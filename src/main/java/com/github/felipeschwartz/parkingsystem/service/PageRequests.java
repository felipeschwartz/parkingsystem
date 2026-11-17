package com.github.felipeschwartz.parkingsystem.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public final class PageRequests {

    public static final int MAX_PAGE_SIZE = 100;

    private PageRequests() {
    }

    // Ordena por id para que a paginação seja estável entre as páginas.
    public static PageRequest of(Integer page, Integer pageSize) {
        if (page == null || page < 0) {
            throw new IllegalArgumentException("Parameter 'page' must be greater than or equal to 0.");
        }
        if (pageSize == null || pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Parameter 'pageSize' must be between 1 and " + MAX_PAGE_SIZE + ".");
        }
        return PageRequest.of(page, pageSize, Sort.by("id"));
    }
}
