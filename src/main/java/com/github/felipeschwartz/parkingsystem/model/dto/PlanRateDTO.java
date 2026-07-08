package com.github.felipeschwartz.parkingsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

public class PlanRateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @JsonIgnore
    private PlanSummaryDTO plan;
    private Long pId;
    private BigDecimal discountPercent;
    private VehicleType vehicleType;
    private Integer durationMonths;
    private BigDecimal monthlyPrice;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlanRateDTO() {
    }

    public PlanRateDTO(Long id, PlanSummaryDTO plan, Long pId, BigDecimal discountPercent, VehicleType vehicleType, Integer durationMonths, BigDecimal monthlyPrice, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.plan = plan;
        this.pId = pId;
        this.discountPercent = discountPercent;
        this.vehicleType = vehicleType;
        this.durationMonths = durationMonths;
        this.monthlyPrice = monthlyPrice;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanSummaryDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanSummaryDTO plan) {
        this.plan = plan;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
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

    @Override
    public String toString() {
        return "PlanRateDTO{" +
                "id=" + id +
                ", pId=" + pId +
                ", vehicleType=" + vehicleType +
                ", durationMonths=" + durationMonths +
                ", monthlyPrice=" + monthlyPrice +
                ", discountPercent=" + discountPercent +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
