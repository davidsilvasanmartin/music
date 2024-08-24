package dev.davidsilva.music.album;

import dev.davidsilva.music.utils.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class AlbumSpecification implements Specification<Album> {
    private final SearchCriteria criteria;

    public AlbumSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Album> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        // More here https://www.baeldung.com/rest-api-search-language-spring-data-specifications
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            return builder.like(
                    root.get(criteria.getKey()), "%" + criteria.getValue() + "%");

        }
        return null;
    }
}
