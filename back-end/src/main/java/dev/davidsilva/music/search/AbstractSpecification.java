package dev.davidsilva.music.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


@Slf4j
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

            // TODO need to understand what kind of sorcery is this
            // TODO this code is duplicating items. For example, an album with genres with names "House" and "Progressive House"
            //  will appear twice on the list when we search by genres:contains:House
            for (SearchCriteria criteria : this.criteria) {
                String key = criteria.key();

                String[] parts = key.split("\\.");
                jakarta.persistence.criteria.Path<?> path = root;

                for (String part : parts) {
                    path = path.get(part);
                }

                switch (criteria.operation()) {
                    case CONTAINS ->
                            predicates.add(builder.like(builder.lower(path.as(String.class)), "%" + criteria.value().toLowerCase() + "%"));
                    case EQUALS -> predicates.add(builder.equal(path, criteria.value()));
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        } catch (Exception e) {
            log.error("Error creating predicate: ", e);
            throw new InvalidSearchException(this.criteria.toString());
        }
    }
}
