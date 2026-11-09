package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.ReservationMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.ReservationDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.model.entity.Reservation;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.repository.ParkingSpaceRepository;
import com.github.felipeschwartz.parkingsystem.repository.ReservationRepository;
import com.github.felipeschwartz.parkingsystem.repository.VehicleRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private final ReservationRepository repository = mock(ReservationRepository.class);
    private final ReservationMapper mapper = mock(ReservationMapper.class);
    private final VehicleRepository vehicleRepository = mock(VehicleRepository.class);
    private final ParkingSpaceRepository spaceRepository = mock(ParkingSpaceRepository.class);
    private final ReservationService service = new ReservationService(repository, mapper, vehicleRepository, spaceRepository);

    private ReservationDTO dto(Long id) {
        return TestDtos.reservation(id);
    }

    private ReservationDTO createRequest(Long vehicleId, Long spaceId) {
        return TestDtos.reservation(null, vehicleId, spaceId);
    }

    @Test
    void findAllShouldMapEveryReservationAndAddLinks() {
        Reservation reservation = new Reservation();
        when(repository.findAll()).thenReturn(List.of(reservation));
        when(mapper.toDTO(reservation)).thenReturn(dto(1L));

        List<ReservationDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        Reservation reservation = new Reservation();
        when(repository.findById(1L)).thenReturn(Optional.of(reservation));
        when(mapper.toDTO(reservation)).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("Reservation not found by ID: 1", e.getMessage());
    }

    @Test
    void createShouldLinkVehicleAndSpaceAndSave() {
        Vehicle vehicle = new Vehicle();
        ParkingSpace space = new ParkingSpace();
        Reservation entity = new Reservation();
        ReservationDTO request = createRequest(1L, 2L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(spaceRepository.findById(2L)).thenReturn(Optional.of(space));
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(5L));

        ReservationDTO result = service.create(request);

        assertSame(vehicle, entity.getVehicle());
        assertSame(space, entity.getParkingSpace());
        assertNotNull(entity.getCreatedAt());
    }

    @Test
    void createShouldFailWhenVehicleIsMissing() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> service.create(createRequest(1L, 2L)));

        assertEquals("Vehicle not found with id: 1", e.getMessage());
    }

    @Test
    void createShouldFailWhenSpaceIsMissing() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(new Vehicle()));
        when(spaceRepository.findById(2L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> service.create(createRequest(1L, 2L)));

        assertEquals("ParkingSpace not found with id: 2", e.getMessage());
    }

    @Test
    void updateShouldApplyDtoWithoutTouchingVehicleWhenNoVehicleIdIsGiven() {
        Reservation entity = new Reservation();
        ReservationDTO input = dto(3L);
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L));

        service.update(input);

        verify(mapper).updateReservationFromDto(input, entity);
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldTakeTheVehicleFromTheReservationReferencedByVehicleId() {
        Vehicle newVehicle = new Vehicle();
        Reservation source = new Reservation();
        source.setVehicle(newVehicle);
        Reservation entity = new Reservation();
        ReservationDTO input = TestDtos.reservation(3L, 8L, null);
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(repository.findById(8L)).thenReturn(Optional.of(source));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L));

        service.update(input);

        assertSame(newVehicle, entity.getVehicle());
    }

    @Test
    void updateShouldFailWhenReservationIsMissing() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update(dto(3L)));
    }

    @Test
    void updateShouldFailWhenReferencedReservationIsMissing() {
        ReservationDTO input = TestDtos.reservation(3L, 8L, null);
        when(repository.findById(3L)).thenReturn(Optional.of(new Reservation()));
        when(repository.findById(8L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.update(input));

        assertEquals("Reservation not found with id: 8", e.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteShouldRemoveExistingReservation() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteShouldFailWhenMissing() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> service.delete(1L));
    }
}
