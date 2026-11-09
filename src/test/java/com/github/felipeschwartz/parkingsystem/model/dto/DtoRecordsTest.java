package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoRecordsTest {

    @Test
    void collectionsShouldDefaultToEmptyAndBeImmutable() {
        assertTrue(TestDtos.vehicle(1L, "A").subscriptionContracts().isEmpty());
        assertTrue(TestDtos.vehicle(1L, "A").sessions().isEmpty());
        assertTrue(TestDtos.plan(1L).rates().isEmpty());
        assertTrue(TestDtos.plan(1L).subscriptionContracts().isEmpty());
        assertTrue(TestDtos.parkingLot(1L).parkingSpaces().isEmpty());
        assertTrue(TestDtos.userIndividual(1L, "1").vehicles().isEmpty());
        assertTrue(TestDtos.userEntity(1L, "1").vehicles().isEmpty());
        assertTrue(TestDtos.createUserRequest(null, null, null).vehicles().isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> TestDtos.plan(1L).rates().add(TestDtos.planRate(1L)));
    }

    @Test
    void collectionsShouldBeDefensivelyCopied() {
        Set<PlanRateDTO> source = new HashSet<>(Set.of(TestDtos.planRate(1L)));
        PlanDTO plan = new PlanDTO(1L, "Gold", null, source, true, null, null);

        source.clear();

        assertEquals(1, plan.rates().size());
    }

    @Test
    void subscriptionContractShouldDefaultToActive() {
        assertEquals(SubscripionStatus.ACTIVE, TestDtos.subscriptionContract(1L).status());
        SubscriptionContractDTO cancelled = new SubscriptionContractDTO(1L, null, null, null, null, null,
                SubscripionStatus.CANCELLED, null, null, null);
        assertEquals(SubscripionStatus.CANCELLED, cancelled.status());
    }

    @Test
    void planRateShouldApplyTheDiscountToTheMonthlyPrice() {
        PlanRateDTO rate = new PlanRateDTO(1L, null, null, new BigDecimal("10"), null, 1, new BigDecimal("200.00"),
                true, null, null);

        assertEquals(new BigDecimal("180.00"), rate.calculatePrice());
    }

    @Test
    void planRateWithoutDiscountShouldKeepThePrice() {
        PlanRateDTO rate = new PlanRateDTO(1L, null, null, null, null, 1, new BigDecimal("99.90"), true, null, null);

        assertEquals(new BigDecimal("99.90"), rate.calculatePrice());
    }

    @Test
    void planRateWithoutPriceShouldFail() {
        assertThrows(IllegalStateException.class, () -> TestDtos.planRate(1L).calculatePrice());
    }

    @Test
    void recordsShouldCompareByValue() {
        assertEquals(TestDtos.hourlyRate(1L), TestDtos.hourlyRate(1L));
        assertNotEquals(TestDtos.hourlyRate(1L), TestDtos.hourlyRate(2L));
    }
}
