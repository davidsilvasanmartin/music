package dev.davidsilva.music.exception;

public class EntityNotFoundException extends AbstractNotFoundException {
    public EntityNotFoundException(String entityClass, int entityId) {
        super(entityClass + " with id " + entityId + " not found");
    }

    public EntityNotFoundException(String entityClass, String entityPrimaryKey, String entityId) {
        super(entityClass + " with " + entityPrimaryKey + " " + entityId + " not found");
    }
}
