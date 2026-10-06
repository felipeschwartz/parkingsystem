package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.ParkingSessionMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.OpenSessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.*;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParkingSessionServiceTest {

    private final ParkingSessionRepository sessionRepository = mock(ParkingSessionRepository.class);
    private final ParkingSpaceRepository spaceRepository = mock(ParkingSpaceRepository.class);

    private final ParkingSessionService service = new ParkingSessionService(
            sessionRepository, spaceRepository, mock(VehicleRepository.class), mock(HourlyRateRepository.class),
            mock(SubscriptionContractRepository.class), mock(ParkingSessionMapper.class));

    @Test
    void shouldReportMissingParkingSpaceWithItsId() {
        when(spaceRepository.findById(9L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> service.openParkingSession(request(9L)));

        assertEquals("ParkingSpace not found with ID: 9", e.getMessage());
    }

    @Test
    void shouldRejectOpeningASecondSessionOnTheSameSpaceAsConflict() {
        ParkingSpace space = new ParkingSpace();
        space.setId(4L);
        when(spaceRepository.findById(4L)).thenReturn(Optional.of(space));
        when(sessionRepository.existsByParkingSpace_IdAndStatus(4L, SessionStatus.OPEN)).thenReturn(true);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> service.openParkingSession(request(4L)));

        assertEquals("There is already an OPEN session for parking space ID: 4", e.getMessage());
    }

    private OpenSessionRequestDTO request(Long spaceId) {
        return new OpenSessionRequestDTO(spaceId, "ABC1234", VehicleType.values()[0], null);
    }
}
