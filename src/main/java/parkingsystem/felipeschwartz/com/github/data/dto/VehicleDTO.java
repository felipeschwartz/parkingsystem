package parkingsystem.felipeschwartz.com.github.data.dto;

import org.springframework.hateoas.RepresentationModel;
import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.entities.ParkingSession;
import parkingsystem.felipeschwartz.com.github.model.entities.SubscriptionContract;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
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
    private Owner owner;
    private Set<SubscriptionContract> subscriptionContracts = new HashSet<>();
    private Set<ParkingSession> sessions = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public VehicleDTO() {
    }

    public VehicleDTO(Long id, String licensePlate, VehicleType type, Owner owner) {
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Set<SubscriptionContract> getSubscriptionContracts() {
        return subscriptionContracts;
    }

    public void setSubscriptionContracts(Set<SubscriptionContract> subscriptionContracts) {
        this.subscriptionContracts = subscriptionContracts;
    }

    public Set<ParkingSession> getSessions() {
        return sessions;
    }

    public void setSessions(Set<ParkingSession> sessions) {
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
