package dev.davidsilva.music.security.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final EntityManager entityManager;

    public UserRepositoryCustomImpl(@Qualifier("appDbEntityManager") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> findByUsernameWithEagerlyFetchedPermissions(String username) {
        // The `FETCH` keyword ensures that the roles and permissions are loaded eagerly in a single query,
        // which is more efficient than letting them load lazily
        String jpql = """
                SELECT DISTINCT u FROM User u
                LEFT JOIN FETCH u.roles r
                LEFT JOIN FETCH r.permissions p
                WHERE u.username = :username
                """;
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("username", username);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public int countAdminUsers() {
        String jpql = """
                SELECT COUNT(DISTINCT u) FROM User u
                JOIN u.roles r
                JOIN r.permissions p
                WHERE p.name = :permissionName
                """;
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("permissionName", "ADMIN");

        return query.getSingleResult().intValue();
    }
}
