package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class VehicleDTO extends RepresentationModel<VehicleDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String licensePlate;
    private VehicleType type;
    private OwnerSummaryDTO owner;
    private Set<SubscriptionContractDTO> subscriptionContracts = new HashSet<>();
    private Set<ParkingSessionDTO> sessions = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public VehicleDTO() {
    }

    public VehicleDTO(Long id, String licensePlate, VehicleType type, OwnerSummaryDTO owner) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public OwnerSummaryDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerSummaryDTO owner) {
        this.owner = owner;
    }

    public Set<SubscriptionContractDTO> getSubscriptionContracts() {
        return subscriptionContracts;
    }

    public void setSubscriptionContracts(Set<SubscriptionContractDTO> subscriptionContracts) {
        this.subscriptionContracts = subscriptionContracts;
    }

    public Set<ParkingSessionDTO> getSessions() {
        return sessions;
    }

    public void setSessions(Set<ParkingSessionDTO> sessions) {
        this.sessions = sessions;
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
        if (!(o instanceof VehicleDTO vehicle)) return false;
        return Objects.equals(getId(), vehicle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
