package parkingsystem.felipeschwartz.com.github.data.dto;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.entities.Plan;
import parkingsystem.felipeschwartz.com.github.model.entities.Vehicle;
import parkingsystem.felipeschwartz.com.github.model.enums.SubscripionStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Objects;

public class SubscriptionContractDTO extends RepresentationModel<SubscriptionContractDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Vehicle vehicle;
    private Plan plan;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscripionStatus status = SubscripionStatus.ACTIVE;
    private Owner owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SubscriptionContractDTO() {}

    public SubscriptionContractDTO(Long id, Vehicle vehicle, Plan plan, LocalDate startDate, LocalDate endDate, SubscripionStatus status, Owner owner) {
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

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
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
