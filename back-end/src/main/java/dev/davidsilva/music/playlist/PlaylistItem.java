package dev.davidsilva.music.playlist;

import dev.davidsilva.music.song.Song;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * TODO
 *  - Link with Album and Artist entities
 *  - Cache artist info
 */

@Entity
@Table(name = "playlist_items", uniqueConstraints = {
        // Matches the UNIQUE (playlist_id, position) constraint in SQL
        @UniqueConstraint(columnNames = {"playlist_id", "position"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"playlist", "song"}) // Avoid recursion with Playlist and Song
@EqualsAndHashCode(of = {"id"})
public class PlaylistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * The position of this item within the playlist.
     * The combination of playlist_id and position must be unique.
     */
    @Column(name = "position", nullable = false)
    private Integer position;

    /**
     * Cached MusicBrainz track ID. Stored for persistence even if the local song is deleted.
     */
    @Column(name = "mb_track_id", nullable = false, length = 255)
    private String mbTrackId;

    /**
     * Cached song title. Stored for persistence.
     */
    @Column(name = "song_title", nullable = false, length = 255)
    private String songTitle;

    /**
     * Cached MusicBrainz album ID. Stored for persistence.
     */
    @Column(name = "mb_album_id", length = 255)
    private String mbAlbumId;

    /**
     * Cached album title. Stored for persistence.
     */
    @Column(name = "album_title", length = 255)
    private String albumTitle;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * The Playlist this item belongs to. This is the owning side of the relationship
     * for the playlist_id foreign key.
     * FetchType.LAZY is used to avoid loading the Playlist unless explicitly needed.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    /**
     * Optional link to the Song entity in the local database.
     * This can be null if the song has been deleted locally.
     * FetchType.LAZY is used to avoid loading the Song unless explicitly needed.
     * The database constraint `ON DELETE SET NULL` handles setting song_id to NULL if the referenced song is deleted.
     * Hibernate's @OnDelete is NOT used here as it doesn't have a direct "SET NULL" equivalent and relies on DB constraints.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = true)
    private Song song;
}