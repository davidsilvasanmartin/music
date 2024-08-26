package dev.davidsilva.music.search;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum SearchOperation {
    CONTAINS("contains"), EQUALS("eq");

    private static final Map<String, SearchOperation> ENUM_MAP;

    static {
        // Build an immutable map of String name to enum pairs.
        Map<String, SearchOperation> map = new HashMap<>();
        for (SearchOperation instance : SearchOperation.values()) {
            map.put(instance.toString(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final String value;

    public static SearchOperation fromString(String value) {
        return ENUM_MAP.get(value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
