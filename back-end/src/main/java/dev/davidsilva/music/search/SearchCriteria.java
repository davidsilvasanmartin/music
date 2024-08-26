package dev.davidsilva.music.search;

import lombok.Getter;

@Getter
public class SearchCriteria {
    private final String key;
    private final SearchOperation operation;
    private final String value;

    public SearchCriteria(String key, SearchOperation operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + ":" + operation + ":" + value;
    }
}
