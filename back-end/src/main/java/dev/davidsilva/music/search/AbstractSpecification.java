package dev.davidsilva.music.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractSpecification<T> implements Specification<T> {
    private final List<SearchCriteria> criteria;

    public AbstractSpecification(List<SearchCriteria> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(
            Root<T> root, CriteriaQuery<?> query,
            CriteriaBuilder builder
    ) {
        try {
            List<Predicate> predicates = new ArrayList<>();

            for (SearchCriteria criteria : this.criteria) {
                switch (criteria.operation()) {
                    case CONTAINS ->
                            predicates.add(builder.like(builder.lower(root.get(criteria.key())), "%" + criteria.value().toLowerCase() + "%"));
                    case EQUALS -> predicates.add(builder.equal(root.get(criteria.key()), criteria.value()));
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        } catch (Exception e) {
            throw new InvalidSearchException(this.criteria.toString());
        }
    }
}
