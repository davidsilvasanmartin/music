package dev.davidsilva.music.search;

public record SearchCriteria(String key, SearchOperation operation, String value) {

    @Override
    public String toString() {
        return key + ":" + operation + ":" + value;
    }
}
