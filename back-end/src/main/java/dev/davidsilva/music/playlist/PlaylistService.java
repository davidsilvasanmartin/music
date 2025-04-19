package dev.davidsilva.music.playlist;

import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistItemRepository playlistItemRepository;
    private final SongRepository songRepository;
    private final PlaylistMapper playlistMapper;

    public PlaylistService(PlaylistRepository playlistRepository, PlaylistItemRepository playlistItemRepository, SongRepository songRepository, PlaylistMapper playlistMapper) {
        this.playlistRepository = playlistRepository;
        this.playlistItemRepository = playlistItemRepository;
        this.songRepository = songRepository;
        this.playlistMapper = playlistMapper;
    }

    /**
     * Creates and persists a new Playlist. If items are provided, their metadata
     * will be populated based on the referenced Song entities.
     *
     * @param playlistDto The Playlist object to create (ID should typically be null).
     *                    Items should have their `song` field set (at least with an ID)
     *                    if metadata population is desired.
     * @return The persisted Playlist with its generated ID and timestamps.
     */
    public PlaylistDto createPlaylist(PlaylistDto playlistDto) {
        Playlist playlist = playlistMapper.toPlaylist(playlistDto);

        // Ensure items list is initialized if null to prevent NullPointerException
        if (playlist.getItems() == null) {
            playlist.setItems(new ArrayList<>());
        }

        int position = 0;
        for (PlaylistItem item : playlist.getItems()) {
            // Set bidirectional relationship
            item.setPlaylist(playlist);
            // Assign position
            item.setPosition(position++);
            // Populate metadata (songTitle, mbTrackId, etc.) from the linked Song
            populateMetadataFromSong(item);
        }

        // Save the playlist; CascadeType.ALL will save associated items
        Playlist createdPlaylist = playlistRepository.save(playlist);
        return playlistMapper.toPlaylistDto(createdPlaylist);
    }

    /**
     * Retrieves all Playlists.
     *
     * @return A list of all Playlists.
     */
    @Transactional(Transactional.TxType.SUPPORTS) // Read-only operation, can join existing tx or run without one
    public List<PlaylistDto> getAllPlaylists() {
        return playlistMapper.toPlaylistDto(playlistRepository.findAll());
    }

    /**
     * Retrieves a specific Playlist by its ID.
     *
     * @param id The ID of the Playlist to retrieve.
     * @return An Optional containing the found Playlist, or empty if not found.
     */
    @Transactional(Transactional.TxType.SUPPORTS) // Read-only
    public PlaylistDto getPlaylistById(Integer id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new PlaylistNotFoundException(id));
        return playlistMapper.toPlaylistDto(playlist);
    }

    /**
     * Updates an existing Playlist, including its items.
     * This method synchronizes the items based on the provided list:
     * - Removes items not present in the new list.
     * - Adds new items present only in the new list.
     * - Updates existing items (primarily their position).
     * - Reorders items according to the new list's order.
     *
     * @param id             The ID of the Playlist to update.
     * @param newPlaylistDto An object containing the new details for the playlist (name, description, items).
     *                       The `items` list should contain the desired state of the playlist items,
     *                       including their intended order. New items should not have an id yet.
     * @return The updated Playlist.
     * @throws EntityNotFoundException if no Playlist with the given ID exists.
     */
    public PlaylistDto updatePlaylist(Integer id, PlaylistDto newPlaylistDto) {
        Playlist newPlaylist = playlistMapper.toPlaylist(newPlaylistDto);

        // 1. Find the existing playlist or throw an exception if not found
        Playlist existingPlaylist = playlistRepository.findById(id)
                .orElseThrow(() -> new PlaylistNotFoundException(id));

        // 2. Update basic Playlist properties
        existingPlaylist.setName(newPlaylist.getName());
        existingPlaylist.setDescription(newPlaylist.getDescription());

        // 3. Synchronize the PlaylistItem collection

        // Get the list of items intended for the updated playlist
        // Use an empty list if the input list is null to simplify logic
        List<PlaylistItem> newItems = Optional.ofNullable(newPlaylist.getItems()).orElseGet(ArrayList::new);

        // --- Strategy: Clear and Re-add ---
        // This leverages CascadeType.ALL and orphanRemoval=true effectively.
        // Hibernate will figure out the necessary INSERTs, UPDATEs, and DELETEs.

        // Clear the existing collection in the managed entity.
        // This marks all previously associated items for removal if they aren't re-added.
        existingPlaylist.getItems().clear();

        // Process the incoming items: set their position, link them to the playlist,
        // and add them back to the managed collection.
        for (int i = 0; i < newItems.size(); i++) {
            // NOTE: any newItem we are updating should have come with its ID present in the newItems list.
            // If not, well it does not matter a lot, because it will be removed and recreated.
            // TODO TEST THE ABOVE
            // TODO One problem that could happen is if the item in DB contains metadata and the newItem does not???
            PlaylistItem newItem = newItems.get(i);
            // Set the position based on the order in the incoming list
            newItem.setPosition(i);
            // Populate metadata (songTitle, etc.) from the referenced Song, before adding to the collection.
            newItem = populateMetadataFromSong(newItem);

            // Ensure the item is correctly linked to the parent playlist
            newItem.setPlaylist(existingPlaylist);

            // Add the item (either new or existing) to the managed collection.
            // If it's new (no ID), CascadeType.PERSIST/MERGE/ALL will save it.
            // If it's existing (has ID), JPA/Hibernate will manage its state (potentially UPDATE).
            existingPlaylist.getItems().add(newItem);
        }

        // 4. Save the Playlist.
        // JPA/Hibernate will flush changes, including:
        // - Updating Playlist metadata (name, description, updatedAt).
        // - Deleting PlaylistItem records that were removed from the collection (orphanRemoval).
        // - Inserting new PlaylistItem records that were added.
        // - Updating existing PlaylistItem records (e.g., their position).
        Playlist updatedPlaylist = playlistRepository.save(existingPlaylist);

        return playlistMapper.toPlaylistDto(updatedPlaylist);
    }


    /**
     * Deletes a Playlist by its ID.
     * If the Playlist has associated PlaylistItem entities, they will also be deleted
     * due to CascadeType.ALL and the database's ON DELETE CASCADE constraint.
     *
     * @param id The ID of the Playlist to delete.
     * @throws EntityNotFoundException if no Playlist with the given ID exists.
     */
    public void deletePlaylist(Integer id) {
        if (!playlistRepository.existsById(id)) {
            throw new PlaylistNotFoundException(id);
        }
        playlistRepository.deleteById(id);
    }

    /**
     * Populates or updates the metadata fields (songTitle, mbTrackId, albumTitle, mbAlbumId)
     * of a PlaylistItem *only if* its referenced Song can be found in the database.
     *
     * @param item The PlaylistItem to process.
     */
    private PlaylistItem populateMetadataFromSong(PlaylistItem item) {
        Song songRef = item.getSong();

        if (songRef == null) {
            // --- No Valid Song Reference Provided ---
            // The input item didn't have a song reference
            // In this case, the input item should already be in the database.
            // We just need to fetch it and update its position
            return getExistingPlaylistItemFromDbWithUpdatedPosition(item);
        } else {
            // At this point, we have a song, but we don't know if it has a valid id (as in, if it exists in the db)
            Optional<Song> songOpt = songRepository.findById(songRef.getId());

            if (songOpt.isEmpty()) {
                // --- Song Not Found ---
                // The Song ID referenced by the item does not exist in the database.
                // This means we are updating the item and NOT creating a new item,
                // because new items should always be created with a valid Song ID.
                // So, again, we take the item from the db, update its position, and return it
                return getExistingPlaylistItemFromDbWithUpdatedPosition(item);
            } else {
                // TODO not sure if it would be beneficial to try and find the item here from the db.
                //  This would be for the case where we are handling updates to an existing item.
                // TODO related: test POSTing a PlaylistItem with cached properties that don't match those
                //  in the database. Should we update the item in the db? Or should we throw an error?
                // --- Song Found ---
                Song fullSong = songOpt.get();
                item.setSong(fullSong);

                // Overwrite metadata from the authoritative source (the found Song entity)
                item.setSongTitle(fullSong.getTitle());
                item.setMbTrackId(fullSong.getMbTrackId());

                // Populate album info if available
                // TODO test this, in case the lazy loading of Album does not work correctly
                if (fullSong.getAlbum() != null) {
                    item.setAlbumTitle(fullSong.getAlbum().getAlbum());
                    item.setMbAlbumId(fullSong.getAlbum().getMbAlbumId());
                } else {
                    throw new RuntimeException("Song " + fullSong.getId() + " has no album");
                }

                return item;
            }
        }
    }

    private PlaylistItem getExistingPlaylistItemFromDbWithUpdatedPosition(PlaylistItem item) {
        Optional<PlaylistItem> existingPlaylistItemOpt = playlistItemRepository.findById(item.getId());
        if (existingPlaylistItemOpt.isEmpty()) {
            throw new PlaylistItemNotFoundException(item.getId());
        }
        PlaylistItem existingPlaylistItem = existingPlaylistItemOpt.get();
        existingPlaylistItem.setPosition(item.getPosition());
        return existingPlaylistItem;
    }
}
