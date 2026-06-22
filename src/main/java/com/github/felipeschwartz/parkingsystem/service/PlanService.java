package com.github.felipeschwartz.parkingsystem.service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.PlanRateRepository;
import com.github.felipeschwartz.parkingsystem.repository.PlanRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanRateRepository planRateRepository;

    public PlanService(PlanRepository planRepository, PlanRateRepository planRateRepository) {
        this.planRepository = planRepository;
        this.planRateRepository = planRateRepository;
    }

    //PLAN
    @Transactional
    public Plan createPlan(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Plan name can't be null or empty");
        }

        Plan p = new Plan();
        p.setName(name.trim());
        p.setActive(true);

        LocalDateTime now = LocalDateTime.now();
        p.setCreatedAt(now);
        p.setUpdatedAt(now);

        return planRepository.save(p);
    }

    @Transactional
    public Plan updatePlan(Long id, String name) {
        Plan p = getPlanOrThrow(id);
        if(name != null && !name.isBlank()) {
            p.setName(name.trim());
        }
        p.setUpdatedAt(LocalDateTime.now());
        return planRepository.save(p);
    }

    @Transactional
    public void activatePlan(Long id) {
        Plan p = getPlanOrThrow(id);
        p.setActive(true);
        p.setUpdatedAt(LocalDateTime.now());
        planRepository.save(p);
    }

    @Transactional
    public void deactivatePlan(Long id) {
        Plan p = getPlanOrThrow(id);
        p.setActive(false);
        p.setUpdatedAt(LocalDateTime.now());
        planRepository.save(p);
    }

    //PLAN RATE
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
            throw new IllegalStateException("Já existe PlanRate para este plan com o mesmo vehicleType e durationMonths.");
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

    @Transactional
    public PlanRate updateRate(Long rateId,
                               BigDecimal monthlyPrice,
                               BigDecimal discountPercent,
                               Boolean active) {

        PlanRate rate = planRateRepository.findById(rateId)
                .orElseThrow(() -> new IllegalArgumentException("PlanRate não encontrado: " + rateId));

        if (monthlyPrice != null) {
            if (monthlyPrice.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price não pode ser negativo.");
            rate.setMonthlyPrice(monthlyPrice);
        }

        if (discountPercent != null) {
            if (discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("discountPercent deve estar entre 0 e 100.");
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
                .orElseThrow(() -> new IllegalArgumentException("PlanRate não encontrado: " + rateId));

        rate.setActive(false);
        rate.setUpdatedAt(LocalDateTime.now());
        planRateRepository.save(rate);
    }

    // --------------------
    // helpers
    // --------------------
    @Transactional(readOnly = true)
    public Plan getPlanOrThrow(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan não encontrado: " + planId));
    }

}
