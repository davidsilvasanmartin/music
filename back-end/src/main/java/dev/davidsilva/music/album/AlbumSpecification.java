package dev.davidsilva.music.album;

import dev.davidsilva.music.search.AbstractSpecification;
import dev.davidsilva.music.search.SearchCriteria;

import java.util.List;


public class AlbumSpecification extends AbstractSpecification<Album> {
    public AlbumSpecification(List<SearchCriteria> criteria) {
        super(criteria);
    }
}
