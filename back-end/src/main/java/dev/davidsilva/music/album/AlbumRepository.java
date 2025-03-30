package dev.davidsilva.music.album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlbumRepository extends JpaRepository<Album, Integer>,
        JpaSpecificationExecutor<Album> {
    boolean existsByBeetsId(int id);
}
