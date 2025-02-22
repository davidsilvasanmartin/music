package dev.davidsilva.music.security.user;

import dev.davidsilva.music.search.AbstractSpecification;
import dev.davidsilva.music.search.SearchCriteria;

import java.util.List;

public class UserSpecification extends AbstractSpecification<User> {
    public UserSpecification(List<SearchCriteria> criteria) {
        super(criteria);
    }
}
