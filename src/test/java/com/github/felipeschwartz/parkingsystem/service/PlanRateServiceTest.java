package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.PlanRateMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanRateDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.repository.PlanRateRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlanRateServiceTest {

    private final PlanRateRepository repository = mock(PlanRateRepository.class);
    private final PlanRateMapper mapper = mock(PlanRateMapper.class);
    private final PlanRateService service = new PlanRateService(repository, mapper);

    private PlanRateDTO dto(Long id) {
        return TestDtos.planRate(id);
    }

    @Test
    void findAllShouldMapEveryRateAndAddLinks() {
        PlanRate rate = new PlanRate();
        when(repository.findAll()).thenReturn(List.of(rate));
        when(mapper.toDTO(rate)).thenReturn(dto(1L));

        List<PlanRateDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        PlanRate rate = new PlanRate();
        when(repository.findById(1L)).thenReturn(Optional.of(rate));
        when(mapper.toDTO(rate)).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("Plan rate not found by ID: 1", e.getMessage());
    }

    @Test
    void createShouldStampTimestampsAndSave() {
        PlanRate entity = new PlanRate();
        PlanRateDTO input = dto(null);
        when(mapper.toEntity(input)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(2L));

        PlanRateDTO result = service.create(input);

        assertEquals(2L, result.id());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldApplyDtoOnExistingEntity() {
        PlanRate entity = new PlanRate();
        PlanRateDTO input = dto(3L);
        when(repository.findById(3L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L));

        PlanRateDTO result = service.update(input);

        verify(mapper).updatePlanRateFromDto(input, entity);
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldFailWhenMissing() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.update(dto(3L)));

        assertEquals("Plan rate not found with id: 3", e.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deleteShouldRemoveExistingRate() {
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

    @Test
    void deactivateShouldFlagRateAsInactive() {
        PlanRate rate = new PlanRate();
        rate.setActive(true);
        when(repository.findById(1L)).thenReturn(Optional.of(rate));

        service.deactivateRate(1L);

        assertFalse(rate.getActive());
        assertNotNull(rate.getUpdatedAt());
        verify(repository).save(rate);
    }

    @Test
    void activateShouldFlagRateAsActive() {
        PlanRate rate = new PlanRate();
        rate.setActive(false);
        when(repository.findById(1L)).thenReturn(Optional.of(rate));

        service.activateRate(1L);

        assertTrue(rate.getActive());
        verify(repository).save(rate);
    }

    @Test
    void activateAndDeactivateShouldFailWhenRateIsMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.activateRate(1L));
        assertThrows(IllegalArgumentException.class, () -> service.deactivateRate(1L));
    }
}
