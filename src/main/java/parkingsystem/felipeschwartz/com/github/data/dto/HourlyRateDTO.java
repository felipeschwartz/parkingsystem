package parkingsystem.felipeschwartz.com.github.data.dto;

import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.converters.VehicleTypeConverter;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class HourlyRateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "vehicle_type", nullable = false, length = 20)
    @Convert(converter = VehicleTypeConverter.class)
    private VehicleType vehicleType;

    @Column(name = "rate_per_hour",  nullable = false,  length = 6)
    private BigDecimal ratePerHour;

    @Column
    private Boolean active;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public HourlyRateDTO() {
    }

    public HourlyRateDTO(VehicleType vehicleType, BigDecimal ratePerHour, Boolean active) {
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
