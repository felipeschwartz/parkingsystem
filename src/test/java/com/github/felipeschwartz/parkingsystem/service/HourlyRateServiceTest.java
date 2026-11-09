package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.HourlyRateMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.repository.HourlyRateRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HourlyRateServiceTest {

    private final HourlyRateRepository repository = mock(HourlyRateRepository.class);
    private final HourlyRateMapper mapper = mock(HourlyRateMapper.class);
    private final HourlyRateService service = new HourlyRateService(repository, mapper);

    private HourlyRateDTO dto(Long id) {
        return TestDtos.hourlyRate(id);
    }

    @Test
    void findAllShouldMapEveryRateAndAddLinks() {
        HourlyRate rate = new HourlyRate();
        when(repository.findAll()).thenReturn(List.of(rate));
        when(mapper.toDTO(rate)).thenReturn(dto(1L));

        List<HourlyRateDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        HourlyRate rate = new HourlyRate();
        when(repository.findById(1L)).thenReturn(Optional.of(rate));
        when(mapper.toDTO(rate)).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("HourlyRate not found by ID: 1", e.getMessage());
    }

    @Test
    void createShouldStampTimestampsAndSave() {
        HourlyRate entity = new HourlyRate();
        HourlyRateDTO input = dto(null);
        when(mapper.toEntity(input)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(2L));

        HourlyRateDTO result = service.create(input);

        assertNotNull(entity.getCreatedAt());
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());
        assertEquals(2L, result.id());
    }

    @Test
    void updateShouldApplyDtoOnExistingEntity() {
        HourlyRate entity = new HourlyRate();
        HourlyRateDTO input = dto(3L);
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
    void deleteShouldRemoveExistingRate() {
        HourlyRate entity = new HourlyRate();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(repository).delete(entity);
    }

    @Test
    void deleteShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.delete(1L));
    }
}
