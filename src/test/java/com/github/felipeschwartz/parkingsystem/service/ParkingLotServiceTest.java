package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.ParkingLotMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingLotDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingLot;
import com.github.felipeschwartz.parkingsystem.repository.ParkingLotRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ParkingLotServiceTest {

    private final ParkingLotRepository repository = mock(ParkingLotRepository.class);
    private final ParkingLotMapper mapper = mock(ParkingLotMapper.class);
    private final ParkingLotService service = new ParkingLotService(repository, mapper);

    private ParkingLotDTO dto(Long id) {
        return TestDtos.parkingLot(id);
    }

    @Test
    void findAllShouldMapEveryLotAndAddLinks() {
        ParkingLot lot = new ParkingLot();
        when(repository.findAll()).thenReturn(List.of(lot));
        when(mapper.toDTO(lot)).thenReturn(dto(1L));

        List<ParkingLotDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        ParkingLot lot = new ParkingLot();
        when(repository.findById(1L)).thenReturn(Optional.of(lot));
        when(mapper.toDTO(lot)).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("ParkingLot not found by ID: 1", e.getMessage());
    }

    @Test
    void createShouldStampTimestampsAndSave() {
        ParkingLot entity = new ParkingLot();
        ParkingLotDTO input = dto(null);
        when(mapper.toEntity(input)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(2L));

        ParkingLotDTO result = service.create(input);

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldApplyDtoOnExistingEntity() {
        ParkingLot entity = new ParkingLot();
        ParkingLotDTO input = dto(3L);
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L));

        service.update(input);

        verify(mapper).updateEntityFromDto(input, entity);
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldFailWhenMissing() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update(dto(3L)));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteShouldRemoveExistingLot() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteShouldFailWhenMissing() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> service.delete(1L));
        verify(repository, never()).deleteById(any());
    }
}
