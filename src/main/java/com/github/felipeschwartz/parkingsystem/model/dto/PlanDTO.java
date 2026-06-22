package com.github.felipeschwartz.parkingsystem.model.dto;

import org.springframework.hateoas.RepresentationModel;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PlanDTO extends RepresentationModel<PlanDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Set<SubscriptionContract> subscriptionContracts =  new HashSet<>();
    private Set<PlanRate> planRates = new HashSet<>();
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlanDTO() {
    }

    public PlanDTO(Long id, String name) {
        this.id = id;
        this.name = name;

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

    public Set<PlanRate> getRates() {
        return planRates;
    }

    public void setRates(Set<PlanRate> planRates) {
        this.planRates = planRates;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlanDTO plan)) return false;
        return Objects.equals(getId(), plan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
