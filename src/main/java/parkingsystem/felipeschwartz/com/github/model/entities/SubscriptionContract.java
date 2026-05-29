package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.enums.ContractStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "subscription_contract")
public class SubscriptionContract implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "plan_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_subscription_contract_plan")
    )
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ContractStatus status = ContractStatus.ACTIVE;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "owner_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_subscription_contract_owner")
    )
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "vehicle_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_subscription_contract_vehicle")
    )
    private Vehicle vehicle;

    public SubscriptionContract() {}

    public SubscriptionContract(Plan plan, LocalDate startDate, LocalDate endDate) {
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ContractStatus.ACTIVE;
    }

    public Long getId() { return id; }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Plan getPlan() { return plan; }
    public ContractStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setPlan(Plan plan) { this.plan = plan; }
    public void setStatus(ContractStatus status) { this.status = status; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isActiveOn(LocalDate date) {
        if (status != ContractStatus.ACTIVE) return false;
        boolean afterStart = !date.isBefore(startDate);
        boolean beforeEnd = (endDate == null) || !date.isAfter(endDate);
        return afterStart && beforeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SubscriptionContract that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
