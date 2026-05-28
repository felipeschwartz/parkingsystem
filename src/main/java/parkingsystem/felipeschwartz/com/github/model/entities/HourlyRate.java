package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;
import model.enums.VehicleType;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table
public class HourlyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_type", nullable = false, length = 10)
    private VehicleType vehicleType;

    @Column(name = "price_per_hour",  nullable = false,  length = 6)
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HourlyRate that)) return false;
        return Objects.equals(getId(), that.getId()) && getVehicleType() == that.getVehicleType() && Objects.equals(getPricePerHour(), that.getPricePerHour());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVehicleType(), getPricePerHour());
    }
}
