package dev.davidsilva.music.security.user;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class UserNotFoundException extends AbstractNotFoundException {
    public UserNotFoundException(String username) {
        super("User with username " + username);
    }
}
