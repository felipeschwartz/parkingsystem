package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "plan_rate")
public class PlanRate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @Column(name = "vehicle_type", length = 2, nullable = false)
    private Integer vehicleType;

    @Column(name = "monthly_price", length = 10, nullable = false)
    private BigDecimal monthlyPrice;

    public PlanRate() {
    }

    public PlanRate(Plan plan, VehicleType vehicleType, BigDecimal monthlyPrice) {
        this.plan = plan;
        setVehicleType(vehicleType);
        this.monthlyPrice = monthlyPrice;
    }


    public Long getId() { return id; }

    public Plan getPlan() { return plan; }

    public VehicleType getVehicleType() {
        return VehicleType.fromCode(vehicleType);
    }

    public BigDecimal getMonthlyPrice() { return monthlyPrice; }

    public void setPlan(Plan plan) { this.plan = plan; }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = (vehicleType == null ? null : vehicleType.getCode());
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlanRate planRate)) return false;
        return Objects.equals(getId(), planRate.getId()) && Objects.equals(getPlan(), planRate.getPlan()) && Objects.equals(getVehicleType(), planRate.getVehicleType()) && Objects.equals(getMonthlyPrice(), planRate.getMonthlyPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPlan(), getVehicleType(), getMonthlyPrice());
    }
}
