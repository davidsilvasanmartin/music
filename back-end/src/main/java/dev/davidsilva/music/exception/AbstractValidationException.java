package dev.davidsilva.music.exception;

public abstract class AbstractValidationException extends RuntimeException {
    public AbstractValidationException(String message) {
        super(message);
    }
}
