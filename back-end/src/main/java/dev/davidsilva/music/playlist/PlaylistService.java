package dev.davidsilva.music.playlist;

import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing Playlists.
 * Handles business logic and coordinates repository interactions.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    /**
     * Creates and persists a new Playlist. If items are provided, their metadata
     * will be populated based on the referenced Song entities.
     *
     * @param playlist The Playlist object to create (ID should typically be null).
     *                 Items should have their `song` field set (at least with an ID)
     *                 if metadata population is desired.
     * @return The persisted Playlist with its generated ID and timestamps.
     */
    public Playlist createPlaylist(Playlist playlist) {
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
        return playlistRepository.save(playlist);
    }

    /**
     * Retrieves all Playlists.
     *
     * @return A list of all Playlists.
     */
    @Transactional(Transactional.TxType.SUPPORTS) // Read-only operation, can join existing tx or run without one
    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    /**
     * Retrieves a specific Playlist by its ID.
     *
     * @param id The ID of the Playlist to retrieve.
     * @return An Optional containing the found Playlist, or empty if not found.
     */
    @Transactional(Transactional.TxType.SUPPORTS) // Read-only
    public Optional<Playlist> getPlaylistById(Integer id) {
        return playlistRepository.findById(id);
    }

    /**
     * Updates an existing Playlist, including its items.
     * This method synchronizes the items based on the provided list:
     * - Removes items not present in the new list.
     * - Adds new items present only in the new list.
     * - Updates existing items (primarily their position).
     * - Reorders items according to the new list's order.
     *
     * @param id          The ID of the Playlist to update.
     * @param newPlaylist An object containing the new details for the playlist (name, description, items).
     *                    The `items` list should contain the desired state of the playlist items,
     *                    including their intended order. New items should not have an id yet.
     * @return The updated Playlist.
     * @throws EntityNotFoundException if no Playlist with the given ID exists.
     */
    public Playlist updatePlaylist(Integer id, Playlist newPlaylist) {
        // 1. Find the existing playlist or throw an exception if not found
        Playlist existingPlaylist = playlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id: " + id));

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
            PlaylistItem newItem = newItems.get(i);

            // Ensure the item is correctly linked to the parent playlist
            newItem.setPlaylist(existingPlaylist);
            // Set the position based on the order in the incoming list
            newItem.setPosition(i);

            // Populate metadata (songTitle, etc.) from the referenced Song, before adding to the collection.
            populateMetadataFromSong(newItem);

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
        return playlistRepository.save(existingPlaylist);
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
            throw new EntityNotFoundException("Playlist not found with id: " + id);
        }
        playlistRepository.deleteById(id);
    }

    /**
     * Populates or updates the metadata fields (songTitle, mbTrackId, albumTitle, mbAlbumId)
     * of a PlaylistItem *only if* its referenced Song can be found in the database.
     * If the Song is found, the metadata is overwritten with the data from the Song entity.
     * If the Song is *not* found (e.g., deleted), the existing metadata on the PlaylistItem
     * is preserved, but the reference to the non-existent Song is removed (`setSong(null)`).
     * If no valid Song reference (ID) is provided initially, the association is cleared.
     *
     * @param item The PlaylistItem to process.
     */
    private void populateMetadataFromSong(PlaylistItem item) {
        /**
         * TODO REVIEW !!!
         */
        Song songRef = item.getSong(); // Get the initial reference from the input item

        if (songRef != null) {
            // We have a potential Song reference with an ID. Try to fetch it.
            Optional<Song> songOpt = songRepository.findById(songRef.getId());

            if (songOpt.isPresent()) {
                // --- Song Found ---
                Song fullSong = songOpt.get();
                // Set the valid, managed Song entity reference on the item
                item.setSong(fullSong);

                // Overwrite metadata from the authoritative source (the found Song entity)
                item.setSongTitle(fullSong.getTitle()); // Assuming Song::getTitle
                item.setMbTrackId(fullSong.getMbTrackId()); // Assuming Song::getMbTrackId

                // Populate album info if available
                if (fullSong.getAlbum() != null) {
                    item.setAlbumTitle(fullSong.getAlbum().getAlbum()); // Assuming Album::getTitle
                    item.setMbAlbumId(fullSong.getAlbum().getMbAlbumId()); // Assuming Album::getMbAlbumId
                } else {
                    // No album associated with the found song, clear album fields
                    item.setAlbumTitle(null);
                    item.setMbAlbumId(null);
                }
            } else {
                // --- Song Not Found ---
                // The Song ID referenced by the item does not exist in the database.
                // Per requirement: Preserve existing metadata, but clear the invalid song association.
                // log.warn("Song with ID {} referenced in PlaylistItem was not found. Preserving existing metadata, but removing song association.", songRef.getId());
                item.setSong(null); // Remove the invalid reference
                // **DO NOT clear item.songTitle, item.mbTrackId, etc. here**
            }
        } else {
            // --- No Valid Song Reference Provided ---
            // The input item didn't have a song reference or the reference lacked an ID.
            // Clear any potentially existing (but now irrelevant) song association.
            // We also clear the metadata here because its source is explicitly invalid/missing.
            if (item.getSong() != null) {
                // log.debug("Clearing song reference on PlaylistItem because input song reference was null or lacked ID.");
                item.setSong(null);
            }
            // It's ambiguous whether metadata should be kept if the *input* is invalid.
            // Clearing seems safer to avoid stale data linked to no song.
            // If preserving metadata even in this case is desired, remove the lines below.
            item.setSongTitle(null);
            item.setMbTrackId(null);
            item.setAlbumTitle(null);
            item.setMbAlbumId(null);
        }
    }

}
