package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.PlanRateRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class PlanRateService  {

    private final PlanRateRepository planRateRepository;

    public PlanRateService(PlanRateRepository planRateRepository) {
        this.planRateRepository = planRateRepository;
    }

    @Transactional
    public PlanRate findByPlanIdAndVehicleType(Long planId, VehicleType vehicleType) {
        return planRateRepository
                .findByPlanIdAndVehicleType(planId, vehicleType)
                .orElseThrow(() -> new RuntimeException(
                        "PlanRate not found for planId=" + planId + " and vehicleType=" + vehicleType
                ));
    }

    /*
    @Transactional
    public PlanRate addRate(Long id, VehicleType vehicleType, Integer durationMonths,
                            BigDecimal monthlyPrice, BigDecimal discountPercent,
                            boolean active) {
        Plan plan = getPlanOrThrow(id);
        Objects.requireNonNull(vehicleType, "Vehicle type can't be null");
        if (durationMonths <= 0) {
            throw new IllegalArgumentException("Duration months can't be negative");
        }
        Objects.requireNonNull(monthlyPrice, "Monthly price can't be null");
        if (monthlyPrice.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new IllegalArgumentException("Monthly price can't be negative");
        }
        if (discountPercent == null) discountPercent = BigDecimal.ZERO;
        if (discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }

        if (planRateRepository.existsByPlan_IdAndVehicleTypeAndDurationMonths(id, vehicleType, durationMonths)) {
            throw new IllegalStateException("A PlanRate already exists for this plan with the same vehicleType and durationMonths..");
        }

        PlanRate rate = new PlanRate();
        rate.setPlan(plan);
        rate.setVehicleType(vehicleType);
        rate.setDurationMonths(durationMonths);
        rate.setMonthlyPrice(monthlyPrice);
        rate.setDiscountPercent(discountPercent);
        rate.setActive(active);

        LocalDateTime now = LocalDateTime.now();
        rate.setCreatedAt(now);
        rate.setUpdatedAt(now);

        return planRateRepository.save(rate);
    }
    */

    @Transactional
    public PlanRate updateRate(Long rateId,
                               BigDecimal monthlyPrice,
                               BigDecimal discountPercent,
                               Boolean active) {

        PlanRate rate = planRateRepository.findById(rateId)
                .orElseThrow(() -> new IllegalArgumentException("PlanRate not found: " + rateId));

        if (monthlyPrice != null) {
            if (monthlyPrice.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price cannot be negative.");
            rate.setMonthlyPrice(monthlyPrice);
        }

        if (discountPercent != null) {
            if (discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("discountPercent must be between 0 and 100.");
            }
            rate.setDiscountPercent(discountPercent);
        }

        if (active != null) {
            rate.setActive(active);
        }

        rate.setUpdatedAt(LocalDateTime.now());
        return planRateRepository.save(rate);
    }

    @Transactional
    public void deactivateRate(Long rateId) {
        PlanRate rate = planRateRepository.findById(rateId)
                .orElseThrow(() -> new IllegalArgumentException("PlanRate not found: " + rateId));

        rate.setActive(false);
        rate.setUpdatedAt(LocalDateTime.now());
        planRateRepository.save(rate);
    }



}
