package dev.davidsilva.music.search;

public class InvalidSearchOperationException extends RuntimeException {
    public InvalidSearchOperationException(String searchOperation) {
        super("Invalid search operation: " + searchOperation);
    }
}
