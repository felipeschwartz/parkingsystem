package com.github.felipeschwartz.parkingsystem.model.entity;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    private static final VehicleType CAR = VehicleType.values()[0];
    private static final VehicleType OTHER = VehicleType.values()[1];

    private long nextId = 1;

    private PlanRate rate(VehicleType type, String price, Plan plan) {
        PlanRate rate = new PlanRate();
        rate.setId(nextId++);
        rate.setVehicleType(type);
        if (price != null) rate.setMonthlyPrice(new BigDecimal(price));
        rate.setPlan(plan);
        rate.setActive(true);
        return rate;
    }

    private Plan planWith(PlanRate... rates) {
        Plan plan = new Plan(1L, "Gold");
        plan.setRates(new HashSet<>(Set.of(rates)));
        return plan;
    }

    @Test
    void validateShouldAcceptAPlanWithDistinctPricedRatesAndAdoptThem() {
        PlanRate car = rate(CAR, "100", null);
        PlanRate other = rate(OTHER, "50", null);
        Plan plan = planWith(car, other);

        assertDoesNotThrow(plan::validate);

        assertSame(plan, car.getPlan());
        assertSame(plan, other.getPlan());
    }

    @Test
    void validateShouldRejectABlankName() {
        Plan plan = planWith(rate(CAR, "100", null));
        plan.setName("  ");
        assertThrows(IllegalArgumentException.class, plan::validate);
        plan.setName(null);
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validateShouldRequireAtLeastOneRate() {
        Plan plan = new Plan(1L, "Gold");
        assertThrows(IllegalArgumentException.class, plan::validate);
        plan.setRates(null);
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validateShouldRejectARateWithoutVehicleType() {
        assertThrows(IllegalArgumentException.class, () -> planWith(rate(null, "100", null)).validate());
    }

    @Test
    void validateShouldRejectNonPositivePrices() {
        assertThrows(IllegalArgumentException.class, () -> planWith(rate(CAR, null, null)).validate());
        assertThrows(IllegalArgumentException.class, () -> planWith(rate(CAR, "0", null)).validate());
        assertThrows(IllegalArgumentException.class, () -> planWith(rate(CAR, "-5", null)).validate());
    }

    @Test
    void validateShouldRejectTwoRatesForTheSameVehicleType() {
        Plan plan = planWith(rate(CAR, "100", null), rate(CAR, "90", null));

        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void isActiveShouldRequireAnActiveRateOfThisPlan() {
        Plan plan = new Plan(1L, "Gold");
        PlanRate own = rate(CAR, "100", plan);
        PlanRate sameId = rate(CAR, "100", new Plan(1L, "Gold copy"));
        PlanRate foreign = rate(CAR, "100", new Plan(2L, "Other"));
        PlanRate inactive = rate(CAR, "100", plan);
        inactive.setActive(false);
        PlanRate orphan = rate(CAR, "100", null);

        assertTrue(plan.isActive(own));
        assertTrue(plan.isActive(sameId));
        assertFalse(plan.isActive(foreign));
        assertFalse(plan.isActive(inactive));
        assertFalse(plan.isActive(orphan));
        assertFalse(plan.isActive(null));
    }
}
