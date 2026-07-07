package com.github.felipeschwartz.parkingsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @JsonManagedReference
    private Set<SubscriptionContract> subscriptionContracts =  new HashSet<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlanRate> rates = new HashSet<>();

    @Column
    private Boolean active;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
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
        return rates;
    }

    public void setRates(Set<PlanRate> planRates) {
        this.rates = planRates;
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

    public Set<SubscriptionContract> getSubscriptionContracts() {
        return subscriptionContracts;
    }

    public void setSubscriptionContracts(Set<SubscriptionContract> subscriptionContracts) {
        this.subscriptionContracts = subscriptionContracts;
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

        if (rates == null || rates.isEmpty()) {
            throw new IllegalArgumentException("Plan deve possuir pelo menos uma tarifa (PlanRate).");
        }

        Set<VehicleType> types = EnumSet.noneOf(VehicleType.class);

        for (PlanRate rate : rates) {
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
