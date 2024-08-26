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

    @JsonProperty(value = "totalPages")
    int totalPages;

    @JsonProperty(value = "isLast")
    boolean isLast;

    public PaginatedResponse(int page, int size, long totalElements, int totalPages, boolean isLast, List<T> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLast = isLast;
        this.content = content;
    }


    public static <From, To> PaginatedResponse<To> fromPage(Page<From> page, ListMapper<From, To> mapper) {
        return new PaginatedResponse<To>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                mapper.map(page.getContent())
        );
    }
}
