package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.SpaceStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;

public class ParkingSpaceSummaryDTO extends RepresentationModel<ParkingSpaceSummaryDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String floor;
    private String position;
    private VehicleType vehicleType;
    private SpaceStatus status;

    public ParkingSpaceSummaryDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpaceSummaryDTO that = (ParkingSpaceSummaryDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}