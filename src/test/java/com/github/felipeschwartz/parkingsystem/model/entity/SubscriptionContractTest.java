package com.github.felipeschwartz.parkingsystem.model.entity;

import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionContractTest {

    private SubscriptionContract contract(SubscripionStatus status, LocalDate start, LocalDate end) {
        SubscriptionContract contract = new SubscriptionContract();
        contract.setStatus(status);
        contract.setStartDate(start);
        contract.setEndDate(end);
        return contract;
    }

    @Test
    void isActiveOnShouldHonourStatusAndDateRange() {
        LocalDate today = LocalDate.now();
        SubscriptionContract open = contract(SubscripionStatus.ACTIVE, today.minusDays(2), null);
        SubscriptionContract bounded = contract(SubscripionStatus.ACTIVE, today.minusDays(2), today.plusDays(2));
        SubscriptionContract cancelled = contract(SubscripionStatus.CANCELLED, today.minusDays(2), null);

        assertTrue(open.isActiveOn(today));
        assertTrue(bounded.isActiveOn(today.plusDays(2)));
        assertFalse(bounded.isActiveOn(today.plusDays(3)));
        assertFalse(bounded.isActiveOn(today.minusDays(3)));
        assertFalse(cancelled.isActiveOn(today));
    }

    @Test
    void renewShouldMoveTheEndDateForward() {
        LocalDate end = LocalDate.now().plusDays(10);
        SubscriptionContract contract = contract(SubscripionStatus.ACTIVE, LocalDate.now().minusDays(5), end);

        contract.renew(end.plusMonths(1));

        assertEquals(end.plusMonths(1), contract.getEndDate());
    }

    @Test
    void renewShouldRejectInvalidContracts() {
        LocalDate today = LocalDate.now();
        assertThrows(IllegalStateException.class,
                () -> contract(SubscripionStatus.CANCELLED, today, today.plusDays(5)).renew(today.plusDays(9)));
        assertThrows(IllegalStateException.class,
                () -> contract(SubscripionStatus.ACTIVE, today, null).renew(today.plusDays(9)));
        assertThrows(IllegalStateException.class,
                () -> contract(SubscripionStatus.ACTIVE, today.minusDays(9), today.minusDays(1)).renew(today.plusDays(9)));
    }

    @Test
    void renewShouldRejectInvalidNewEndDates() {
        LocalDate today = LocalDate.now();
        SubscriptionContract contract = contract(SubscripionStatus.ACTIVE, today.minusDays(1), today.plusDays(5));

        assertThrows(IllegalArgumentException.class, () -> contract.renew(null));
        assertThrows(IllegalArgumentException.class, () -> contract.renew(today.plusDays(5)));
        assertThrows(IllegalArgumentException.class, () -> contract.renew(today.plusDays(1)));
    }

    @Test
    void cancelShouldMarkTheContractAsCancelled() {
        SubscriptionContract contract = contract(SubscripionStatus.ACTIVE, LocalDate.now(), null);

        contract.cancel(1L);

        assertEquals(SubscripionStatus.CANCELLED, contract.getStatus());
        assertFalse(contract.isActive());
    }

    @Test
    void isActiveShouldFollowTheStatus() {
        assertTrue(contract(SubscripionStatus.ACTIVE, LocalDate.now(), null).isActive());
    }

    @Test
    void equalsShouldCompareById() {
        SubscriptionContract a = new SubscriptionContract();
        SubscriptionContract b = new SubscriptionContract();
        a.setId(1L);
        b.setId(1L);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        b.setId(2L);
        assertNotEquals(a, b);
    }
}
