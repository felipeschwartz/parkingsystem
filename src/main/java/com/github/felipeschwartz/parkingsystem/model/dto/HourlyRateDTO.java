package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class HourlyRateDTO extends RepresentationModel<HourlyRateDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private VehicleType vehicleType;
    private BigDecimal ratePerHour;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HourlyRateDTO() {
    }

    public HourlyRateDTO(Long id, VehicleType vehicleType, BigDecimal ratePerHour, Boolean active) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.ratePerHour = ratePerHour;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public BigDecimal getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(BigDecimal pricePerHour) {
        this.ratePerHour = pricePerHour;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof HourlyRateDTO that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
