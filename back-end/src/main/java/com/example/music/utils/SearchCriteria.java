package com.example.music.utils;

public class SearchCriteria {
    private final String key;
    private final String operation;
    private final String value;

    public SearchCriteria(String key, String operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    public String getValue() {
        return value;
    }
}
