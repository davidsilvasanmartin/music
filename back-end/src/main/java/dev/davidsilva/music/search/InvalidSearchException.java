package dev.davidsilva.music.search;

public class InvalidSearchException extends RuntimeException {
    public InvalidSearchException(String search) {
        super("Invalid search: " + search);
    }
}
