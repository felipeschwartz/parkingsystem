package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

public class PlanRateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private PlanDTO plan;
    private VehicleType vehicleType;
    private Integer durationMonths;
    private BigDecimal monthlyPrice;
    private BigDecimal discountPercent;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlanRateDTO() {
    }

    public PlanRateDTO(Long id, PlanDTO plan, VehicleType vehicleType, Integer durationMonths, BigDecimal monthlyPrice, BigDecimal discountPercent, Boolean active) {
        this.id = id;
        this.plan = plan;
        this.vehicleType = vehicleType;
        this.durationMonths = durationMonths;
        this.monthlyPrice = monthlyPrice;
        this.discountPercent = discountPercent;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanDTO plan) {
        this.plan = plan;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal disccount) {
        this.discountPercent = disccount;
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
        if (!(o instanceof PlanRateDTO planRate)) return false;
        return Objects.equals(getId(), planRate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public BigDecimal calculatePrice() {
        if (monthlyPrice == null) {
            throw new IllegalStateException("monthlyPrice não pode ser nulo");
        }

        BigDecimal discountPercent = (this.discountPercent == null)
                ? BigDecimal.ZERO
                : this.discountPercent;

        BigDecimal discountFactor = discountPercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        return monthlyPrice
                .subtract(monthlyPrice.multiply(discountFactor))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
