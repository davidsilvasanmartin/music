package dev.davidsilva.music.exception;

public abstract class AbstractNotFoundException extends RuntimeException {
    public AbstractNotFoundException(String message) {
        super(message);
    }
}
