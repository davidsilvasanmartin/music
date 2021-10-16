package blog.cybertricks.music.musicservice.album;

import blog.cybertricks.music.musicservice.utils.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
