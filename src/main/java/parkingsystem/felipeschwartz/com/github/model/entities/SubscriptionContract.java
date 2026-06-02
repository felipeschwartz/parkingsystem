package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.enums.SubscripionStatus;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
            name = "vehicle_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_subscription_contract_vehicle")
    )
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "plan_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_subscription_contract_plan")
    )
    private Plan plan;


    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SubscripionStatus status = SubscripionStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "owner_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_subscription_contract_owner")
    )
    private Owner owner;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public SubscriptionContract() {}

    public SubscriptionContract(Long id, Vehicle vehicle, Plan plan, LocalDate startDate, LocalDate endDate, SubscripionStatus status, Owner owner) {
        this.id = id;
        this.vehicle = vehicle;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public SubscripionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscripionStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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
        if (!(o instanceof SubscriptionContract that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public boolean isActiveOn(LocalDate date) {
        if (status != SubscripionStatus.ACTIVE) return false;
        boolean afterStart = !date.isBefore(startDate);
        boolean beforeEnd = (endDate == null) || !date.isAfter(endDate);
        return afterStart && beforeEnd;
    }

    public void renew(LocalDate newEndDate) {
        if (status != SubscripionStatus.ACTIVE) {
            throw new IllegalStateException("Só é possível renovar contrato ACTIVE.");
        }

        if (this.endDate == null) {
            throw new IllegalStateException("Contrato sem endDate não pode ser renovado por este método.");
        }

        LocalDate today = LocalDate.now();

        // sua regra: não renova se já venceu
        if (today.isAfter(this.endDate)) {
            throw new IllegalStateException("Contrato vencido. Não é possível renovar.");
        }

        if (newEndDate == null) {
            throw new IllegalArgumentException("newEndDate não pode ser nulo.");
        }

        // renovação deve estender (não encurtar, nem manter igual)
        if (!newEndDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("newEndDate deve ser após o endDate atual.");
        }

        this.endDate = newEndDate;
    }
}
