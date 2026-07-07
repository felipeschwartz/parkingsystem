package com.github.felipeschwartz.parkingsystem.model.dto;

import java.util.List;

public class PlanRequestDTO {
    private String name;
    private Boolean active;
    private List<Long> rateIds; // Para receber apenas os IDs das Rates
    private List<Long> subscriptionContractIds;

    public PlanRequestDTO() {
    }

    public PlanRequestDTO(String name, Boolean active, List<Long> rateIds, List<Long> subscriptionContractIds) {
        this.name = name;
        this.active = active;
        this.rateIds = rateIds;
        this.subscriptionContractIds = subscriptionContractIds;
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
