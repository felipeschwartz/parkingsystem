package parkingsystem.felipeschwartz.com.github.data.dto;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import parkingsystem.felipeschwartz.com.github.model.entities.ParkingLot;
import parkingsystem.felipeschwartz.com.github.model.entities.Reservation;
import parkingsystem.felipeschwartz.com.github.model.enums.SpaceStatus;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ParkingSpaceDTO extends RepresentationModel<ParkingSpaceDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String floor;
    private String position;
    private VehicleType vehicleType;
    private SpaceStatus status;
    private Boolean active;
    private ParkingLot parkingLot;
    private Set<Reservation> reservation = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ParkingSpaceDTO() {
    }

    public ParkingSpaceDTO(Long id, String floor, String position, VehicleType vehicleType, SpaceStatus status, Boolean active, ParkingLot parkingLot) {
        this.id = id;
        this.floor = floor;
        this.position = position;
        this.vehicleType = vehicleType;
        this.status = status;
        this.active = active;
        this.parkingLot = parkingLot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public SpaceStatus getStatus() {
        return status;
    }

    public void setStatus(SpaceStatus status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public Set<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(Set<Reservation> reservation) {
        this.reservation = reservation;
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
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpaceDTO that = (ParkingSpaceDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}