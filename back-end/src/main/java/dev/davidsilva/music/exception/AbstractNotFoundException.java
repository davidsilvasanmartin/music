package dev.davidsilva.music.exception;

import jakarta.persistence.EntityNotFoundException;

public abstract class AbstractNotFoundException extends EntityNotFoundException {
    public AbstractNotFoundException(String entity, long id) {
        this(entity, String.valueOf(id));
    }

    public AbstractNotFoundException(String entity, String id) {
        super(entity + " with id " + id + " was not found");
    }

    public AbstractNotFoundException(String messageIdentifyingNotFoundEntity) {
        super(messageIdentifyingNotFoundEntity + " was not found");
    }
}
