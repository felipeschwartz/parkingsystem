package com.github.felipeschwartz.parkingsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubscriptionContractDTO extends RepresentationModel<SubscriptionContractDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private VehicleSummaryDTO vehicle;
    @JsonIgnore
    private PlanSummaryDTO plan;
    private Long pId;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscripionStatus status = SubscripionStatus.ACTIVE;
    private UserSummaryDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SubscriptionContractDTO() {}

    public SubscriptionContractDTO(Long id, VehicleSummaryDTO vehicle, PlanSummaryDTO plan, Long pId, LocalDate startDate, LocalDate endDate, SubscripionStatus status, UserSummaryDTO user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.vehicle = vehicle;
        this.plan = plan;
        this.pId = pId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanSummaryDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanSummaryDTO plan) {
        this.plan = plan;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
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

    public UserSummaryDTO getUser() {
        return user;
    }

    public void setUser(UserSummaryDTO user) {
        this.user = user;
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
