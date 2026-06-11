package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "plan")
public class Plan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;
    
    @OneToMany(mappedBy = "plan")
    private Set<SubscriptionContract> subscriptionContracts =  new HashSet<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlanRate> planRates = new HashSet<>();

    @Column
    private Boolean active;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public Plan() {
    }

    public Plan(Long id, String name) {
        this.id = id;
        this.name = name;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PlanRate> getRates() {
        return planRates;
    }

    public void setRates(Set<PlanRate> planRates) {
        this.planRates = planRates;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Plan plan)) return false;
        return Objects.equals(getId(), plan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public void validate(){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Plan.name não pode ser vazio.");
        }

        if (planRates == null || planRates.isEmpty()) {
            throw new IllegalArgumentException("Plan deve possuir pelo menos uma tarifa (PlanRate).");
        }

        Set<VehicleType> types = EnumSet.noneOf(VehicleType.class);

        for (PlanRate rate : planRates) {
            if (rate.getVehicleType() == null) {
                throw new IllegalArgumentException("PlanRate.vehicleType não pode ser nulo.");
            }
            if (rate.getMonthlyPrice() == null || rate.getMonthlyPrice().signum() <= 0) {
                throw new IllegalArgumentException("PlanRate.monthlyPrice deve ser maior que zero.");
            }
            if (!types.add(rate.getVehicleType())) {
                throw new IllegalArgumentException("Tarifa duplicada para vehicleType=" + rate.getVehicleType());
            }

            if (rate.getPlan() != this) {
                rate.setPlan(this);
            }
        }
    }

    public boolean isActive(PlanRate chosenRate) {
        if (chosenRate == null) return false;

        boolean belongsToThisPlan =
                chosenRate.getPlan() == this
                        || (this.id != null
                        && chosenRate.getPlan() != null
                        && Objects.equals(chosenRate.getPlan().getId(), this.id));

        if (!belongsToThisPlan) return false;

        // Flag: null => false, true => true
        return Boolean.TRUE.equals(chosenRate.getActive());
    }

}
