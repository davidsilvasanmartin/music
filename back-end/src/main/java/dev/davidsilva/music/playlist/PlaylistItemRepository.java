package dev.davidsilva.music.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistItemRepository extends JpaRepository<PlaylistItem, Integer> {
}
