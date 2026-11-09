package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.ParkingSessionMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.OpenSessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.SpaceStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.*;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ParkingSessionServiceFlowTest {

    private static final VehicleType TYPE = VehicleType.values()[0];

    private final ParkingSessionRepository sessionRepository = mock(ParkingSessionRepository.class);
    private final ParkingSpaceRepository spaceRepository = mock(ParkingSpaceRepository.class);
    private final VehicleRepository vehicleRepository = mock(VehicleRepository.class);
    private final HourlyRateRepository hourlyRateRepository = mock(HourlyRateRepository.class);
    private final SubscriptionContractRepository contractRepository = mock(SubscriptionContractRepository.class);
    private final ParkingSessionMapper mapper = mock(ParkingSessionMapper.class);

    private final ParkingSessionService service = new ParkingSessionService(sessionRepository, spaceRepository,
            vehicleRepository, hourlyRateRepository, contractRepository, mapper);

    private ParkingSessionDTO dto(Long id) {
        return TestDtos.parkingSession(id);
    }

    private ParkingSpace space() {
        ParkingSpace space = new ParkingSpace();
        space.setId(4L);
        space.setStatus(SpaceStatus.AVAILABLE);
        return space;
    }

    private void stubSave() {
        when(sessionRepository.save(any(ParkingSession.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDTO(any(ParkingSession.class))).thenReturn(dto(1L));
    }

    private ParkingSession openSession(Vehicle vehicle, ParkingSpace space, LocalDateTime entry) {
        ParkingSession session = new ParkingSession();
        session.setId(10L);
        session.setVehicle(vehicle);
        session.setVehicleType(TYPE);
        session.setParkingSpace(space);
        session.open(entry);
        return session;
    }

    private HourlyRate rate(String perHour) {
        HourlyRate rate = mock(HourlyRate.class);
        when(rate.getRatePerHour()).thenReturn(new BigDecimal(perHour));
        return rate;
    }

    @Test
    void openShouldCreateAnHourlySessionForAnUnknownVehicle() {
        ParkingSpace space = space();
        LocalDateTime entry = LocalDateTime.of(2026, 1, 1, 10, 0);
        when(spaceRepository.findById(4L)).thenReturn(Optional.of(space));
        when(vehicleRepository.findVehicleByLicensePlate("ABC1234")).thenReturn(Optional.empty());
        stubSave();

        ParkingSessionDTO result = service.openParkingSession(new OpenSessionRequestDTO(4L, "ABC1234", TYPE, entry));

        assertEquals(SpaceStatus.OCCUPIED, space.getStatus());
        verify(spaceRepository).save(space);
        verify(sessionRepository).save(argThat(s -> s.getVehicle() == null
                && "ABC1234".equals(s.getLicensePlate()) && entry.equals(s.getEntryTime())));
    }

    @Test
    void openShouldCreateASubscriptionSessionWhenVehicleHasAnActiveContract() {
        ParkingSpace space = space();
        Vehicle vehicle = new Vehicle();
        vehicle.setId(7L);
        vehicle.setLicensePlate("ABC1234");
        vehicle.setType(TYPE);
        when(spaceRepository.findById(4L)).thenReturn(Optional.of(space));
        when(vehicleRepository.findVehicleByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicle));
        when(contractRepository.existsByVehicleIdAndStatusAndEndDateGreaterThanEqual(eq(7L),
                eq(SubscripionStatus.ACTIVE), any())).thenReturn(true);
        stubSave();

        service.openParkingSession(new OpenSessionRequestDTO(4L, "ABC1234", TYPE, null));

        verify(sessionRepository).save(argThat(s -> s.getVehicle() == vehicle));
    }

    @Test
    void openShouldChargeHourlyWhenKnownVehicleHasNoActiveContract() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(7L);
        when(spaceRepository.findById(4L)).thenReturn(Optional.of(space()));
        when(vehicleRepository.findVehicleByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicle));
        when(contractRepository.existsByVehicleIdAndStatusAndEndDateGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(false);
        stubSave();

        service.openParkingSession(new OpenSessionRequestDTO(4L, "ABC1234", TYPE, null));

        verify(sessionRepository).save(argThat(s -> s.getVehicle() == null));
    }

    @Test
    void closeShouldChargeByTheElapsedTimeAndFreeTheSpace() {
        ParkingSpace space = space();
        space.setStatus(SpaceStatus.OCCUPIED);
        LocalDateTime entry = LocalDateTime.of(2026, 1, 1, 10, 0);
        ParkingSession session = openSession(null, space, entry);
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        HourlyRate rate = rate("10.00");
        when(hourlyRateRepository.findByVehicleTypeAndActiveTrue(TYPE)).thenReturn(Optional.of(rate));
        stubSave();

        ParkingSessionDTO result = service.closeSession(10L, entry.plusHours(2));

        assertEquals(SessionStatus.CLOSED, session.getStatus());
        assertEquals(new BigDecimal("20.00"), session.getAmountCharged());
        assertEquals(SpaceStatus.AVAILABLE, space.getStatus());
        verify(spaceRepository).save(space);
    }

    @Test
    void closeShouldUseNowWhenNoExitTimeIsGivenAndSkipSpaceWhenAbsent() {
        ParkingSession session = openSession(null, null, LocalDateTime.now().minusMinutes(30));
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        HourlyRate rate = rate("10.00");
        when(hourlyRateRepository.findByVehicleTypeAndActiveTrue(TYPE)).thenReturn(Optional.of(rate));
        stubSave();

        service.closeSession(10L, null);

        assertNotNull(session.getExitTime());
        verify(spaceRepository, never()).save(any());
    }

    @Test
    void closeShouldBeFreeWhenVehicleHasAnActiveContractOnTheEntryDay() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(7L);
        LocalDateTime entry = LocalDateTime.of(2026, 1, 1, 10, 0);
        ParkingSession session = openSession(vehicle, space(), entry);
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(contractRepository.hasActiveContractForDate(eq(7L), eq(SubscripionStatus.ACTIVE), any()))
                .thenReturn(true);
        stubSave();

        service.closeSession(10L, entry.plusHours(3));

        assertEquals(new BigDecimal("0.00"), session.getAmountCharged());
        verifyNoInteractions(hourlyRateRepository);
    }

    @Test
    void closeShouldFallBackToHourlyRateWhenVehicleHasNoContract() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(7L);
        LocalDateTime entry = LocalDateTime.of(2026, 1, 1, 10, 0);
        ParkingSession session = openSession(vehicle, space(), entry);
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(contractRepository.hasActiveContractForDate(anyLong(), any(), any())).thenReturn(false);
        HourlyRate rate = rate("5.00");
        when(hourlyRateRepository.findByVehicleTypeAndActiveTrue(TYPE)).thenReturn(Optional.of(rate));
        stubSave();

        service.closeSession(10L, entry.plusHours(1));

        assertEquals(new BigDecimal("5.00"), session.getAmountCharged());
    }

    @Test
    void closeShouldFailWhenNoActiveHourlyRateExists() {
        LocalDateTime entry = LocalDateTime.of(2026, 1, 1, 10, 0);
        ParkingSession session = openSession(null, space(), entry);
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(hourlyRateRepository.findByVehicleTypeAndActiveTrue(TYPE)).thenReturn(Optional.empty());

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> service.closeSession(10L, entry.plusHours(1)));

        assertEquals("No active HourlyRate found for vehicle type: " + TYPE, e.getMessage());
    }

    @Test
    void closeShouldRejectASessionThatIsNotOpen() {
        ParkingSession session = new ParkingSession();
        session.setStatus(SessionStatus.CLOSED);
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.closeSession(10L, null));

        assertEquals("Only OPEN sessions can be closed.", e.getMessage());
    }

    @Test
    void closeShouldFailWhenSessionIsMissing() {
        when(sessionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.closeSession(10L, null));
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        ParkingSession session = new ParkingSession();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(mapper.toDTO(session)).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("Parking Session not found by ID: 1", e.getMessage());
    }

    @Test
    void findAllShouldMapEverySessionAndAddLinks() {
        ParkingSession session = new ParkingSession();
        when(sessionRepository.findAll()).thenReturn(List.of(session));
        when(mapper.toDTO(session)).thenReturn(dto(1L));

        List<ParkingSessionDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findOpenSessionsByParkingSpaceShouldMapTheRepositoryResult() {
        ParkingSession session = new ParkingSession();
        when(sessionRepository.findByParkingSpaceIdAndStatus(4L, SessionStatus.OPEN)).thenReturn(List.of(session));
        when(mapper.toDTO(session)).thenReturn(dto(1L));

        assertEquals(1, service.findOpenSessionsByParkingSpace(4L).size());
    }

    @Test
    void findSessionsByVehicleShouldMapTheRepositoryResult() {
        ParkingSession session = new ParkingSession();
        when(sessionRepository.findByVehicleId(7L)).thenReturn(List.of(session));
        when(mapper.toDTO(session)).thenReturn(dto(1L));

        assertEquals(1, service.findSessionsByVehicle(7L).size());
    }
}
