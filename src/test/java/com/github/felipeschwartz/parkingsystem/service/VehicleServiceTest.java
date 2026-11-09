package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.VehicleMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.SubscriptionContractRepository;
import com.github.felipeschwartz.parkingsystem.repository.VehicleRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.NoActiveContractException;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    private final VehicleRepository repository = mock(VehicleRepository.class);
    private final SubscriptionContractRepository contractRepository = mock(SubscriptionContractRepository.class);
    private final VehicleMapper mapper = mock(VehicleMapper.class);
    private final VehicleService service = new VehicleService(repository, contractRepository, mapper);

    private VehicleDTO dto(Long id, String plate) {
        return TestDtos.vehicle(id, plate);
    }

    @Test
    void findAllShouldMapEveryVehicleAndAddLinks() {
        Vehicle vehicle = new Vehicle();
        when(repository.findAll()).thenReturn(List.of(vehicle));
        when(mapper.toDTO(vehicle)).thenReturn(dto(1L, "ABC1234"));

        List<VehicleDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        Vehicle vehicle = new Vehicle();
        when(repository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(mapper.toDTO(vehicle)).thenReturn(dto(1L, "ABC1234"));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("Vehicle not found by ID: 1", e.getMessage());
    }

    @Test
    void createShouldStampTimestampsAndSave() {
        Vehicle entity = new Vehicle();
        VehicleDTO input = dto(null, "ABC1234");
        when(repository.existsByLicensePlate("ABC1234")).thenReturn(false);
        when(mapper.toEntity(input)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(2L, "ABC1234"));

        VehicleDTO result = service.create(input);

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void createShouldRejectNullVehicle() {
        assertThrows(NullPointerException.class, () -> service.create(null));
    }

    @Test
    void createShouldRejectBlankLicensePlate() {
        assertThrows(IllegalArgumentException.class, () -> service.create(dto(null, " ")));
        assertThrows(IllegalArgumentException.class, () -> service.create(dto(null, null)));
    }

    @Test
    void createShouldRejectDuplicatedLicensePlateAsConflict() {
        when(repository.existsByLicensePlate("ABC1234")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.create(dto(null, "ABC1234")));
        verify(repository, never()).save(any());
    }

    @Test
    void updateShouldOnlyOverwriteProvidedFields() {
        Vehicle entity = new Vehicle();
        entity.setLicensePlate("OLD1234");
        entity.setType(VehicleType.values()[0]);
        VehicleDTO input = dto(3L, null);
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L, "OLD1234"));

        service.update(input);

        assertEquals("OLD1234", entity.getLicensePlate());
        assertEquals(VehicleType.values()[0], entity.getType());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldOverwriteLicensePlateAndTypeWhenProvided() {
        Vehicle entity = new Vehicle();
        VehicleType newType = VehicleType.values()[VehicleType.values().length - 1];
        VehicleDTO input = new VehicleDTO(3L, "NEW1234", newType, null, null, null, null, null);
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L, "NEW1234"));

        service.update(input);

        assertEquals("NEW1234", entity.getLicensePlate());
        assertEquals(newType, entity.getType());
    }

    @Test
    void updateShouldFailWhenMissing() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update(dto(3L, "X")));
    }

    @Test
    void deleteShouldRemoveExistingVehicle() {
        Vehicle entity = new Vehicle();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(repository).delete(entity);
    }

    @Test
    void deleteShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void findByLicensePlateShouldReturnDtoWithLinks() {
        Vehicle entity = new Vehicle();
        when(repository.findVehicleByLicensePlate("ABC1234")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto(1L, "ABC1234"));

        assertEquals("ABC1234", service.findByLicensePlate("ABC1234").licensePlate());
    }

    @Test
    void findByLicensePlateShouldFailWhenMissing() {
        when(repository.findVehicleByLicensePlate("ABC1234")).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> service.findByLicensePlate("ABC1234"));

        assertEquals("Vehicle not found: ABC1234", e.getMessage());
    }

    @Test
    void hasActiveContractShouldReflectTheRepository() {
        Vehicle vehicle = new Vehicle();
        when(repository.findVehicleByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicle));
        when(contractRepository.findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE))
                .thenReturn(Optional.of(new SubscriptionContract()))
                .thenReturn(Optional.empty());

        assertTrue(service.hasActiveContract("ABC1234"));
        assertFalse(service.hasActiveContract("ABC1234"));
    }

    @Test
    void findActiveContractShouldReturnTheContract() {
        Vehicle vehicle = new Vehicle();
        SubscriptionContract contract = new SubscriptionContract();
        when(repository.findVehicleByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicle));
        when(contractRepository.findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE))
                .thenReturn(Optional.of(contract));

        assertSame(contract, service.findActiveContract("ABC1234"));
    }

    @Test
    void findActiveContractShouldFailWhenVehicleHasNone() {
        Vehicle vehicle = new Vehicle();
        when(repository.findVehicleByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicle));
        when(contractRepository.findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(NoActiveContractException.class, () -> service.findActiveContract("ABC1234"));
    }
}
