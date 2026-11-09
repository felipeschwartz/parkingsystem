package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.CycleAvoidingMappingContext;
import com.github.felipeschwartz.parkingsystem.mapper.PlanMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanCreationDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.PlanRateRepository;
import com.github.felipeschwartz.parkingsystem.repository.PlanRepository;
import com.github.felipeschwartz.parkingsystem.repository.SubscriptionContractRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlanServiceTest {

    private final PlanMapper mapper = mock(PlanMapper.class);
    private final PlanRepository repository = mock(PlanRepository.class);
    private final PlanRateRepository rateRepository = mock(PlanRateRepository.class);
    private final SubscriptionContractRepository contractRepository = mock(SubscriptionContractRepository.class);
    private final PlanService service = new PlanService(mapper, repository, mock(CycleAvoidingMappingContext.class),
            rateRepository, contractRepository);

    private PlanDTO dto(Long id) {
        return TestDtos.plan(id);
    }

    private PlanRate rate(VehicleType type, String price) {
        PlanRate rate = new PlanRate();
        rate.setVehicleType(type);
        rate.setMonthlyPrice(new BigDecimal(price));
        return rate;
    }

    @Test
    void findAllShouldMapEveryPlanAndAddLinks() {
        Plan plan = new Plan();
        when(repository.findAll()).thenReturn(List.of(plan));
        when(mapper.toDTO(eq(plan), any(CycleAvoidingMappingContext.class))).thenReturn(dto(1L));

        List<PlanDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        Plan plan = new Plan();
        when(repository.findById(1L)).thenReturn(Optional.of(plan));
        when(mapper.toDTO(eq(plan), any(CycleAvoidingMappingContext.class))).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void createShouldAttachRatesAndContractsToThePlan() {
        Plan plan = new Plan(null, "Gold");
        PlanRate rate = rate(VehicleType.values()[0], "100.00");
        SubscriptionContract contract = new SubscriptionContract();
        PlanCreationDTO input = new PlanCreationDTO("Gold", true, List.of(5L), List.of(7L));
        when(mapper.toEntity(input)).thenReturn(plan);
        when(repository.save(plan)).thenReturn(plan);
        when(rateRepository.findAllById(List.of(5L))).thenReturn(List.of(rate));
        when(contractRepository.findAllById(List.of(7L))).thenReturn(List.of(contract));
        when(mapper.toDTO(eq(plan), any(CycleAvoidingMappingContext.class))).thenReturn(dto(9L));

        PlanDTO result = service.create(input);

        assertSame(plan, rate.getPlan());
        assertSame(plan, contract.getPlan());
        assertTrue(plan.getRates().contains(rate));
        assertTrue(plan.getSubscriptionContracts().contains(contract));
    }

    @Test
    void createShouldRejectAPlanWithoutRates() {
        Plan plan = new Plan(null, "Empty");
        PlanCreationDTO input = new PlanCreationDTO("Empty", true, null, null);
        when(mapper.toEntity(input)).thenReturn(plan);
        when(repository.save(plan)).thenReturn(plan);

        assertThrows(IllegalArgumentException.class, () -> service.create(input));
    }

    @Test
    void updateShouldApplyDtoOnExistingPlan() {
        Plan plan = new Plan();
        PlanDTO input = dto(3L);
        when(repository.findById(3L)).thenReturn(Optional.of(plan));
        when(mapper.toDTO(eq(plan), any(CycleAvoidingMappingContext.class))).thenReturn(dto(3L));

        service.update(input);

        verify(mapper).updateEntityFromDTO(input, plan);
        assertNotNull(plan.getUpdatedAt());
    }

    @Test
    void updateShouldFailWhenMissing() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update(dto(3L)));
    }

    @Test
    void activateAndDeactivateShouldToggleThePlan() {
        Plan plan = new Plan();
        when(repository.findById(1L)).thenReturn(Optional.of(plan));

        service.activatePlan(1L);
        assertTrue(plan.getActive());

        service.deactivatePlan(1L);
        assertFalse(plan.getActive());

        verify(repository, times(2)).save(plan);
    }

    @Test
    void activateAndDeactivateShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.activatePlan(1L));
        assertThrows(ObjectNotFoundException.class, () -> service.deactivatePlan(1L));
    }

    @Test
    void deletePlanShouldRemoveExistingPlan() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deletePlan(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deletePlanShouldFailWhenMissing() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> service.deletePlan(1L));
    }

    @Test
    void getPlanOrThrowShouldReturnOrFail() {
        Plan plan = new Plan();
        when(repository.findById(1L)).thenReturn(Optional.of(plan));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertSame(plan, service.getPlanOrThrow(1L));
        assertThrows(IllegalArgumentException.class, () -> service.getPlanOrThrow(2L));
    }
}
