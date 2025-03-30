package dev.davidsilva.music.artist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Optional<Artist> findByMbArtistId(String artistName);
}
