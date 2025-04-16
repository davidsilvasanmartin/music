package dev.davidsilva.music.artist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer>, JpaSpecificationExecutor<Artist> {
    Optional<Artist> findByMbArtistId(String artistName);
}
