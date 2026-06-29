package com.github.felipeschwartz.parkingsystem.model.dto;

import org.springframework.hateoas.RepresentationModel;
import com.github.felipeschwartz.parkingsystem.model.entity.Owner;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Objects;

public class SubscriptionContractDTO extends RepresentationModel<SubscriptionContractDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private VehicleSummaryDTO vehicle;
    private PlanDTO plan;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscripionStatus status = SubscripionStatus.ACTIVE;
    private OwnerSummaryDTO owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SubscriptionContractDTO() {}

    public SubscriptionContractDTO(Long id, VehicleSummaryDTO vehicle, PlanDTO plan, LocalDate startDate, LocalDate endDate, SubscripionStatus status, OwnerSummaryDTO owner) {
        this.id = id;
        this.vehicle = vehicle;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanDTO plan) {
        this.plan = plan;
    }

    public SubscripionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscripionStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public OwnerSummaryDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerSummaryDTO owner) {
        this.owner = owner;
    }

    public VehicleSummaryDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleSummaryDTO vehicle) {
        this.vehicle = vehicle;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SubscriptionContractDTO that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
