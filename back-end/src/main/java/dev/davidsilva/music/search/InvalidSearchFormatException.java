package dev.davidsilva.music.search;

public class InvalidSearchFormatException extends RuntimeException {
    public InvalidSearchFormatException(String search) {
        super("Invalid search format: " + search);
    }
}
