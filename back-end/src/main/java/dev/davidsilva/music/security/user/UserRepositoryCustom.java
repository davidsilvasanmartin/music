package dev.davidsilva.music.security.user;

import java.util.Optional;

public interface UserRepositoryCustom {
    /**
     * Retrieves a User entity by its username with the associated permissions eagerly fetched. This is necessary because
     * permissions are tried to be accessed somewhere in Spring Security's code. The default implementation of findByUsername
     * in UserRepository fetches permissions lazily. When Spring Security code tries to fetch the permissions, it does
     * so outside a Transaction, which causes the application to crash. Hence, we need to fetch the permissions eagerly.
     *
     * @param username the username of the user to fetch
     * @return an Optional containing the User entity with permissions if found, or an empty Optional otherwise
     */
    Optional<User> findByUsernameWithEagerlyFetchedPermissions(String username);

    int countAdminUsers();
}
