package com.github.felipeschwartz.parkingsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageRequestsTest {

    @Test
    void shouldBuildAPageRequestSortedById() {
        PageRequest request = PageRequests.of(2, 5);

        assertEquals(2, request.getPageNumber());
        assertEquals(5, request.getPageSize());
        assertEquals(Sort.by("id"), request.getSort());
    }

    @Test
    void shouldAcceptTheMaximumPageSize() {
        assertEquals(PageRequests.MAX_PAGE_SIZE, PageRequests.of(0, PageRequests.MAX_PAGE_SIZE).getPageSize());
    }

    @Test
    void shouldRejectANegativeOrMissingPage() {
        assertThrows(IllegalArgumentException.class, () -> PageRequests.of(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> PageRequests.of(null, 10));
    }

    @Test
    void shouldRejectAPageSizeOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> PageRequests.of(0, 0));
        assertThrows(IllegalArgumentException.class, () -> PageRequests.of(0, PageRequests.MAX_PAGE_SIZE + 1));
        assertThrows(IllegalArgumentException.class, () -> PageRequests.of(0, null));
    }
}
