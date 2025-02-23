package dev.davidsilva.music.security.user;

import dev.davidsilva.music.exception.AbstractValidationException;

public class CannotDeleteLastAdminUserException extends AbstractValidationException {
    public CannotDeleteLastAdminUserException() {
        super("Cannot delete last admin user");
    }
}
