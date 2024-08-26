package dev.davidsilva.music.album;

import dev.davidsilva.music.search.InvalidSearchException;
import dev.davidsilva.music.search.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class AlbumSpecification implements Specification<Album> {
    private final List<SearchCriteria> criteria;

    public AlbumSpecification(List<SearchCriteria> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(
            Root<Album> root, CriteriaQuery<?> query,
            CriteriaBuilder builder
    ) {
        try {
            List<Predicate> predicates = new ArrayList<>();

            for (SearchCriteria criteria : this.criteria) {
                switch (criteria.getOperation()) {
                    case CONTAINS ->
                            predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toLowerCase() + "%"));
                    case EQUALS -> predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        } catch (Exception e) {
            throw new InvalidSearchException(this.criteria.toString());
        }
        // More here https://www.baeldung.com/rest-api-search-language-spring-data-specifications
//        if (criteria.getOperation().equalsIgnoreCase(":")) {
//            return builder.like(
//                    root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
//
//        }
//        return null;
    }
}
