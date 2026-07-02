package com.github.felipeschwartz.parkingsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlanCreationDTO {
    @NotBlank(message = "Plan name cannot be null or empty")
    @Size(min = 3, max = 100, message = "Plan name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Active status cannot be null")
    private Boolean active;

    public PlanCreationDTO() {
    }

    public PlanCreationDTO(String name, Boolean active) {
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
