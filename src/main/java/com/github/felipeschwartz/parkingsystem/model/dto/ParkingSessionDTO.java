package com.github.felipeschwartz.parkingsystem.model.dto;

import org.springframework.hateoas.RepresentationModel;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.model.entity.Payment;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingSessionDTO extends RepresentationModel<ParkingSessionDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Vehicle vehicle;
    private String licensePlate;
    private VehicleType vehicleType;
    private ParkingSpace parkingSpace;
    private Payment payment;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private SessionStatus status;
    private BigDecimal amountCharged;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ParkingSessionDTO() {
    }

    public ParkingSessionDTO(Long id, Vehicle vehicle, ParkingSpace parkingSpace,
                             LocalDateTime entryTime, LocalDateTime exitTime,
                             SessionStatus status, BigDecimal amountCharged) {
        this.id = id;
        this.vehicle = vehicle;
        this.parkingSpace = parkingSpace;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.status = status;
        this.amountCharged = amountCharged;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        if (vehicle != null) {
            this.licensePlate = vehicle.getLicensePlate();
            this.vehicleType = vehicle.getType();
        }
    }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
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
        ParkingSessionDTO that = (ParkingSessionDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
