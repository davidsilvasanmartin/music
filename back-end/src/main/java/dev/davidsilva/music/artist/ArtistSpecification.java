package dev.davidsilva.music.artist;

import dev.davidsilva.music.search.AbstractSpecification;
import dev.davidsilva.music.search.SearchCriteria;
import jakarta.persistence.criteria.*;

import java.util.List;


public class ArtistSpecification extends AbstractSpecification<Artist> {
    public ArtistSpecification(List<SearchCriteria> criteria) {
        super(criteria);
    }

    public Predicate toPredicate(
            Root<Artist> root, CriteriaQuery<?> query,
            CriteriaBuilder builder
    ) {
        assert query != null;

        query.distinct(true);

        if (query.getResultType() != null && query.getResultType().equals(Artist.class)) {
            root.fetch("albums", JoinType.LEFT);
        } else {
            root.join("albums", JoinType.LEFT);
        }

        return super.toPredicate(root, query, builder);
    }
}
