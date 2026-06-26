package com.github.felipeschwartz.parkingsystem.model.dto;

import java.util.Objects;

public class PlanSummaryDTO {

    private Long id;
    private String name;

    public PlanSummaryDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlanSummaryDTO that = (PlanSummaryDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
