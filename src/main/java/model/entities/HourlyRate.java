package model.entities;

import model.enums.VehicleType;

import java.math.BigDecimal;

public class HourlyRate {
    private Long id;
    private VehicleType vehicleType;
    private BigDecimal pricePerHour;

    public HourlyRate() {
    }

    public HourlyRate(VehicleType vehicleType, BigDecimal pricePerHour) {
        this.vehicleType = vehicleType;
        this.pricePerHour = pricePerHour;
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

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}
