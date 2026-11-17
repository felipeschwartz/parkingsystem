package com.github.felipeschwartz.parkingsystem.model.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PagedResponse<T>(List<T> data, PaginationResponse pagination) {

    public static <S, T> PagedResponse<T> from(Page<S> page, Function<? super S, ? extends T> mapper) {
        List<T> data = page.getContent().stream().<T>map(mapper).toList();
        return new PagedResponse<>(data, new PaginationResponse(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()));
    }
}
