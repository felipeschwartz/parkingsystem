package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;


public class VehicleSummaryDTO extends RepresentationModel<VehicleSummaryDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String licensePlate;
    private VehicleType type;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VehicleSummaryDTO vehicle)) return false;
        return Objects.equals(getId(), vehicle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
