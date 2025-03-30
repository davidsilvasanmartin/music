package dev.davidsilva.music.beets.item;

import dev.davidsilva.music.beets.album.BeetsAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeetsItemRepository extends JpaRepository<BeetsAlbum, Integer> {
}
