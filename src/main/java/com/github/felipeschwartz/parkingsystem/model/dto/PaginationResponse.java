package com.github.felipeschwartz.parkingsystem.model.dto;

public record PaginationResponse(Integer page, Integer pageSize, Long totalElements, Integer totalPages) {
}
