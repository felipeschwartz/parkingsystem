package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.ReservationStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ReservationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private VehicleSummaryDTO vehicle;
    private ParkingSpaceDTO parkingSpace;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public ReservationDTO() {
    }

    public ReservationDTO(Long id, VehicleSummaryDTO vehicle, ParkingSpaceDTO parkingSpace, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus status) {
        this.id = id;
        this.vehicle = vehicle;
        this.parkingSpace = parkingSpace;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
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

    public ParkingSpaceDTO getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpaceDTO parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
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
        ReservationDTO that = (ReservationDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}