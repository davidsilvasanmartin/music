package dev.davidsilva.music.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedResponse<T> {
    @JsonProperty(value = "content")
    private final List<T> content;

    @JsonProperty(value = "page")
    int page;

    @JsonProperty(value = "size")
    int size;

    @JsonProperty(value = "totalElements")
    long totalElements;

    public PaginatedResponse(int page, int size, long totalElements, List<T> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.content = content;
    }


    public static <From, To> PaginatedResponse<To> fromPage(Page<From> page, ListMapper<From, To> mapper) {
        return new PaginatedResponse<To>(
                // The Page is 0-indexed, but we expose 1-indexed page parameter
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                mapper.map(page.getContent())
        );
    }
}
