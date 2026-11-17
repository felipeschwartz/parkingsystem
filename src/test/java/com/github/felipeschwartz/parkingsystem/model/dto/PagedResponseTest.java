package com.github.felipeschwartz.parkingsystem.model.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PagedResponseTest {

    @Test
    void shouldMapTheContentAndCopyThePaginationMetadata() {
        PageImpl<Integer> page = new PageImpl<>(List.of(1, 2), PageRequest.of(1, 2), 5);

        PagedResponse<String> response = PagedResponse.from(page, n -> "n" + n);

        assertEquals(List.of("n1", "n2"), response.data());
        assertEquals(new PaginationResponse(1, 2, 5L, 3), response.pagination());
    }
}
