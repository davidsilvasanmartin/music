package dev.davidsilva.music.playlist;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"items"}) // Avoid recursion/large output in toString
@EqualsAndHashCode(of = {"id"}) // Base equality on ID
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Matches SERIAL type in PostgreSQL
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // TODO do we need @CreationTimestamp?? It has updatable=false...
    @CreationTimestamp // Automatically set on creation by Hibernate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically set on update by Hibernate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(
            // Mapped by: indicates the 'playlist' field in the PlaylistItem entity owns the relationship.
            mappedBy = "playlist", // Field name in PlaylistItem that maps back to this Playlist
            // Cascade: if you save a `Playlist`, its associated new `PlaylistItem`s are also saved. If you delete a
            // `Playlist`, its `PlaylistItem`s are also deleted (matching the ON DELETE CASCADE in SQL)
            cascade = CascadeType.ALL,
            // Orphan removal: if you remove a `PlaylistItem` from the `items` list _within the managed
            // `Playlist` entity_ and save the `Playlist`, JPA will delete that `PlaylistItem` row from the database
            orphanRemoval = true
    )
    @OrderBy("position ASC") // Keep the list ordered by the position column
    private List<PlaylistItem> items = new ArrayList<>();

    // Convenience method to add items and maintain bidirectional relationship
    public void addItem(PlaylistItem item) {
        if (item != null) {
            items.add(item);
            item.setPlaylist(this);
            // TODO: Managing the 'position' field requires separate logic,
            //  typically when adding/removing/reordering items.
        }
    }

    // Convenience method to remove items and maintain bidirectional relationship
    public void removeItem(PlaylistItem item) {
        if (item != null) {
            items.remove(item);
            item.setPlaylist(null);
            // TODO: recalculate positions
        }
    }
}