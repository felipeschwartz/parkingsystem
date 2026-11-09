package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.ParkingSpaceMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSpaceDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.repository.ParkingSpaceRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ParkingSpaceServiceTest {

    private final ParkingSpaceRepository repository = mock(ParkingSpaceRepository.class);
    private final ParkingSpaceMapper mapper = mock(ParkingSpaceMapper.class);
    private final ParkingSpaceService service = new ParkingSpaceService(repository, mapper);

    private ParkingSpaceDTO dto(Long id) {
        return TestDtos.parkingSpace(id);
    }

    @Test
    void findAllShouldMapEverySpaceAndAddLinks() {
        ParkingSpace space = new ParkingSpace();
        when(repository.findAll()).thenReturn(List.of(space));
        when(mapper.toDTO(space)).thenReturn(dto(1L));

        List<ParkingSpaceDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        ParkingSpace space = new ParkingSpace();
        when(repository.findById(1L)).thenReturn(Optional.of(space));
        when(mapper.toDTO(space)).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("Parking Space not found by ID: 1", e.getMessage());
    }

    @Test
    void createShouldStampTimestampsAndSave() {
        ParkingSpace entity = new ParkingSpace();
        ParkingSpaceDTO input = dto(null);
        when(mapper.toEntity(input)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(2L));

        ParkingSpaceDTO result = service.createParkingSpace(input);

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldApplyDtoOnExistingEntity() {
        ParkingSpace entity = new ParkingSpace();
        ParkingSpaceDTO input = dto(3L);
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L));

        service.updateParkingSpace(input);

        verify(mapper).updateEntityFromDto(input, entity);
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldFailWhenMissing() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.updateParkingSpace(dto(3L)));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteShouldRemoveExistingSpace() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteParkingSpace(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteShouldFailWhenMissing() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> service.deleteParkingSpace(1L));
        verify(repository, never()).deleteById(any());
    }
}
