package com.github.felipeschwartz.parkingsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.Link;

import java.util.List;

public class PlanCreationDTO {
    @NotBlank(message = "Plan name cannot be null or empty")
    @Size(min = 3, max = 100, message = "Plan name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Active status cannot be null")
    private Boolean active;

    private List<Long> rateIds;
    private List<Long> subscriptionContractIds;

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

    public List<Long> getRateIds() {
        return rateIds;
    }

    public void setRateIds(List<Long> rateIds) {
        this.rateIds = rateIds;
    }

    public List<Long> getSubscriptionContractIds() {
        return subscriptionContractIds;
    }

    public void setSubscriptionContractIds(List<Long> subscriptionContractIds) {
        this.subscriptionContractIds = subscriptionContractIds;
    }

}
