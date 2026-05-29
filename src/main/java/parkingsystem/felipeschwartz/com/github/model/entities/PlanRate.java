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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 20)
    private VehicleType vehicleType;

    @Column(name = "monthly_price", length = 10, nullable = false)
    private BigDecimal monthlyPrice;

    public PlanRate() {
    }

    public PlanRate(Plan plan, VehicleType vehicleType, BigDecimal monthlyPrice) {
        this.plan = plan;
        this.vehicleType = vehicleType;
        this.monthlyPrice = monthlyPrice;
    }

    public Long getId() { return id; }

    public Plan getPlan() { return plan; }

    public VehicleType getVehicleType() { return vehicleType; }

    public BigDecimal getMonthlyPrice() { return monthlyPrice; }

    public void setPlan(Plan plan) { this.plan = plan; }

    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public void setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlanRate planRate)) return false;
        return Objects.equals(getId(), planRate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
