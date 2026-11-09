package com.github.felipeschwartz.parkingsystem.model.entity;

import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSessionTest {

    private static final VehicleType TYPE = VehicleType.values()[0];
    private static final LocalDateTime ENTRY = LocalDateTime.of(2026, 1, 1, 10, 0);

    private ParkingSession openSession() {
        return ParkingSession.forHourly("ABC1234", TYPE, new ParkingSpace(), ENTRY);
    }

    @Test
    void forHourlyShouldOpenAnAnonymousSession() {
        ParkingSession session = openSession();

        assertNull(session.getVehicle());
        assertEquals("ABC1234", session.getLicensePlate());
        assertEquals(SessionStatus.OPEN, session.getStatus());
        assertEquals(ENTRY, session.getEntryTime());
        assertNotNull(session.getCreatedAt());
    }

    @Test
    void forHourlyShouldRejectMissingArguments() {
        ParkingSpace space = new ParkingSpace();
        assertThrows(NullPointerException.class, () -> ParkingSession.forHourly(null, TYPE, space, ENTRY));
        assertThrows(NullPointerException.class, () -> ParkingSession.forHourly("A", null, space, ENTRY));
        assertThrows(NullPointerException.class, () -> ParkingSession.forHourly("A", TYPE, null, ENTRY));
        assertThrows(NullPointerException.class, () -> ParkingSession.forHourly("A", TYPE, space, null));
    }

    @Test
    void forSubscriptionShouldCopyThePlateAndTypeFromTheVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("XYZ9876");
        vehicle.setType(TYPE);

        ParkingSession session = ParkingSession.forSubscription(vehicle, new ParkingSpace(), ENTRY);

        assertSame(vehicle, session.getVehicle());
        assertEquals("XYZ9876", session.getLicensePlate());
        assertEquals(TYPE, session.getVehicleType());
    }

    @Test
    void forSubscriptionShouldRejectMissingArguments() {
        ParkingSpace space = new ParkingSpace();
        assertThrows(NullPointerException.class, () -> ParkingSession.forSubscription(null, space, ENTRY));
        assertThrows(NullPointerException.class, () -> ParkingSession.forSubscription(new Vehicle(), null, ENTRY));
        assertThrows(NullPointerException.class, () -> ParkingSession.forSubscription(new Vehicle(), space, null));
    }

    @Test
    void openShouldRejectAnAlreadyOpenOrClosedSession() {
        ParkingSession session = openSession();
        assertThrows(IllegalStateException.class, () -> session.open(ENTRY));

        session.close(ENTRY.plusHours(1));
        assertThrows(IllegalStateException.class, () -> session.open(ENTRY));
        assertThrows(NullPointerException.class, () -> new ParkingSession().open(null));
    }

    @Test
    void closeShouldRecordTheExitTime() {
        ParkingSession session = openSession();

        session.close(ENTRY.plusHours(2));

        assertEquals(SessionStatus.CLOSED, session.getStatus());
        assertEquals(ENTRY.plusHours(2), session.getExitTime());
    }

    @Test
    void closeShouldRejectInvalidStates() {
        ParkingSession session = openSession();
        assertThrows(NullPointerException.class, () -> session.close(null));
        assertThrows(IllegalArgumentException.class, () -> session.close(ENTRY.minusMinutes(1)));

        ParkingSession noEntry = new ParkingSession();
        noEntry.setStatus(SessionStatus.OPEN);
        assertThrows(IllegalStateException.class, () -> noEntry.close(ENTRY));

        session.close(ENTRY.plusHours(1));
        assertThrows(IllegalStateException.class, () -> session.close(ENTRY.plusHours(2)));
    }

    @Test
    void durationShouldBeZeroWithoutEntryAndNeverNegative() {
        assertEquals(Duration.ZERO, new ParkingSession().getDuration());

        ParkingSession inconsistent = openSession();
        inconsistent.setExitTime(ENTRY.minusHours(1));
        assertEquals(Duration.ZERO, inconsistent.getDuration());

        ParkingSession stillOpen = ParkingSession.forHourly("A", TYPE, new ParkingSpace(), LocalDateTime.now().minusHours(1));
        assertTrue(stillOpen.getDuration().toMinutes() >= 59);
    }

    @Test
    void calculateAmountShouldPriceTheElapsedHours() {
        ParkingSession session = openSession();
        session.close(ENTRY.plusMinutes(90));

        BigDecimal amount = session.calculateAmount(new BigDecimal("10.00"));

        assertEquals(new BigDecimal("15.00"), amount);
        assertEquals(amount, session.getAmountCharged());
    }

    @Test
    void calculateAmountShouldRejectInvalidInput() {
        ParkingSession session = openSession();
        assertThrows(IllegalArgumentException.class, () -> session.calculateAmount(new BigDecimal("-1")));
        assertThrows(NullPointerException.class, () -> session.calculateAmount(null));
        assertThrows(IllegalStateException.class, () -> session.calculateAmount(BigDecimal.TEN));
    }

    @Test
    void calculateAmountShouldBeFreeForAVehicleWithAnActiveContract() {
        SubscriptionContract contract = new SubscriptionContract();
        contract.setStatus(SubscripionStatus.ACTIVE);
        contract.setStartDate(LocalDate.of(2025, 1, 1));
        Vehicle vehicle = new Vehicle();
        vehicle.setSubscriptionContracts(Set.of(contract));
        ParkingSession session = ParkingSession.forSubscription(vehicle, new ParkingSpace(), ENTRY);
        session.close(ENTRY.plusHours(5));

        assertEquals(BigDecimal.ZERO, session.calculateAmount(BigDecimal.TEN));
        assertEquals(BigDecimal.ZERO, session.getAmountCharged());
    }

    @Test
    void equalsShouldCompareById() {
        ParkingSession a = new ParkingSession();
        ParkingSession b = new ParkingSession();
        a.setId(1L);
        b.setId(1L);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        b.setId(2L);
        assertNotEquals(a, b);
        assertNotEquals(a, null);
        assertNotEquals(a, "x");
    }
}
