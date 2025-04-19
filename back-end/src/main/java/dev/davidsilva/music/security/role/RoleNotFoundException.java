package dev.davidsilva.music.security.role;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class RoleNotFoundException extends AbstractNotFoundException {
    public RoleNotFoundException(String name) {
        super("Role with name " + name);
    }
}
