package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingSessionSummaryDTO extends RepresentationModel<ParkingSessionSummaryDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private VehicleSummaryDTO vehicle;
    private String licensePlate;
    private VehicleType vehicleType;
    private ParkingSpaceDTO parkingSpace;
    private Long paymentId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private SessionStatus status;
    private BigDecimal amountCharged;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ParkingSessionSummaryDTO() {
    }

    public ParkingSessionSummaryDTO(Long id, VehicleSummaryDTO vehicle, String licensePlate, VehicleType vehicleType, ParkingSpaceDTO parkingSpace, Long paymentId, LocalDateTime entryTime, LocalDateTime exitTime, SessionStatus status, BigDecimal amountCharged, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.vehicle = vehicle;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.parkingSpace = parkingSpace;
        this.paymentId = paymentId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.status = status;
        this.amountCharged = amountCharged;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleSummaryDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleSummaryDTO vehicle) {
        this.vehicle = vehicle;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public ParkingSpaceDTO getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpaceDTO parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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
        ParkingSessionSummaryDTO that = (ParkingSessionSummaryDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
