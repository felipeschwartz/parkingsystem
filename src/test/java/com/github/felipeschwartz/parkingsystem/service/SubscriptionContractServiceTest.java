package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.CycleAvoidingMappingContext;
import com.github.felipeschwartz.parkingsystem.mapper.SubscriptionContractMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.*;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.repository.*;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriptionContractServiceTest {

    private final SubscriptionContractRepository repository = mock(SubscriptionContractRepository.class);
    private final SubscriptionContractMapper mapper = mock(SubscriptionContractMapper.class);
    private final PlanRepository planRepository = mock(PlanRepository.class);
    private final VehicleRepository vehicleRepository = mock(VehicleRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final SubscriptionContractService service = new SubscriptionContractService(repository, mapper,
            mock(CycleAvoidingMappingContext.class), planRepository, vehicleRepository, userRepository);

    private SubscriptionContractDTO dto(Long id) {
        return TestDtos.subscriptionContract(id);
    }

    private SubscriptionContractDTO createRequest(LocalDate start, LocalDate end) {
        return TestDtos.subscriptionContract(1L, 2L, 3L, start, end);
    }

    private SubscriptionContract activeContract(LocalDate end) {
        SubscriptionContract contract = new SubscriptionContract();
        contract.setStatus(SubscripionStatus.ACTIVE);
        contract.setStartDate(LocalDate.now().minusDays(5));
        contract.setEndDate(end);
        return contract;
    }

    @Test
    void findAllShouldMapEveryContractAndAddLinks() {
        SubscriptionContract contract = new SubscriptionContract();
        when(repository.findAllWithUsers()).thenReturn(List.of(contract));
        when(mapper.toDTO(eq(contract), any(CycleAvoidingMappingContext.class))).thenReturn(dto(1L));

        List<SubscriptionContractDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        SubscriptionContract contract = new SubscriptionContract();
        when(repository.findById(1L)).thenReturn(Optional.of(contract));
        when(mapper.toDTO(eq(contract), any(CycleAvoidingMappingContext.class))).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("Subscription contract with id 1 not found!", e.getMessage());
    }

    @Test
    void createShouldWireRelationsAndRegisterContractOnThePlan() {
        Plan plan = new Plan(1L, "Gold");
        Vehicle vehicle = new Vehicle();
        User user = new UserIndividual();
        SubscriptionContract entity = new SubscriptionContract();
        SubscriptionContractDTO request = createRequest(LocalDate.now(), LocalDate.now().plusMonths(1));
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(vehicleRepository.findById(2L)).thenReturn(Optional.of(vehicle));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(eq(entity), any(CycleAvoidingMappingContext.class))).thenReturn(dto(9L));

        SubscriptionContractDTO result = service.create(request);

        assertSame(plan, entity.getPlan());
        assertSame(vehicle, entity.getVehicle());
        assertSame(user, entity.getUser());
        assertTrue(plan.getSubscriptionContracts().contains(entity));
        verify(planRepository).save(plan);
    }

    @Test
    void createShouldCreateTheContractSetWhenPlanHasNone() {
        Plan plan = new Plan(1L, "Gold");
        plan.setSubscriptionContracts(null);
        SubscriptionContract entity = new SubscriptionContract();
        SubscriptionContractDTO request = createRequest(LocalDate.now(), null);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(vehicleRepository.findById(2L)).thenReturn(Optional.of(new Vehicle()));
        when(userRepository.findById(3L)).thenReturn(Optional.of(new UserIndividual()));
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(eq(entity), any(CycleAvoidingMappingContext.class))).thenReturn(dto(9L));

        service.create(request);

        assertEquals(1, plan.getSubscriptionContracts().size());
    }

    @Test
    void createShouldRejectStartDateInThePast() {
        SubscriptionContractDTO request = createRequest(LocalDate.now().minusDays(1), null);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.create(request));

        assertEquals("startDate cannot be null or before today.", e.getMessage());
    }

    @Test
    void createShouldRejectMissingStartDate() {
        assertThrows(IllegalArgumentException.class, () -> service.create(createRequest(null, null)));
    }

    @Test
    void createShouldRejectEndDateBeforeStartDate() {
        SubscriptionContractDTO request = createRequest(LocalDate.now().plusDays(5), LocalDate.now().plusDays(1));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.create(request));

        assertEquals("endDate cannot be earlier than startDate.", e.getMessage());
    }

    @Test
    void createShouldFailWhenPlanIsMissing() {
        when(planRepository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> service.create(createRequest(LocalDate.now(), null)));

        assertEquals("Plan with id 1 not found!", e.getMessage());
    }

    @Test
    void createShouldFailWhenVehicleIsMissing() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(new Plan(1L, "Gold")));
        when(vehicleRepository.findById(2L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> service.create(createRequest(LocalDate.now(), null)));

        assertEquals("Vehicle with id 2 not found!", e.getMessage());
    }

    @Test
    void createShouldFailWhenUserIsMissing() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(new Plan(1L, "Gold")));
        when(vehicleRepository.findById(2L)).thenReturn(Optional.of(new Vehicle()));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> service.create(createRequest(LocalDate.now(), null)));

        assertEquals("User with id 3 not found!", e.getMessage());
    }

    @Test
    void updateShouldApplyDtoOnExistingContract() {
        SubscriptionContract entity = new SubscriptionContract();
        SubscriptionContractDTO input = dto(4L);
        when(repository.findById(4L)).thenReturn(Optional.of(entity));
        when(mapper.toDTO(eq(entity), any(CycleAvoidingMappingContext.class))).thenReturn(dto(4L));

        SubscriptionContractDTO result = service.update(input);

        verify(mapper).updateSubscriptionContractFromDto(input, entity);
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldFailWhenMissing() {
        when(repository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update(dto(4L)));
    }

    @Test
    void deleteShouldRemoveExistingContract() {
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
    void renewShouldExtendTheEndDate() {
        LocalDate newEnd = LocalDate.now().plusMonths(3);
        SubscriptionContract entity = activeContract(LocalDate.now().plusMonths(1));
        SubscriptionContractDTO input = TestDtos.subscriptionContract(null, null, null, null, newEnd);
        when(repository.findById(4L)).thenReturn(Optional.of(entity));
        when(mapper.toDTO(eq(entity), any(CycleAvoidingMappingContext.class))).thenReturn(dto(4L));

        service.renew(4L, input);

        assertEquals(newEnd, entity.getEndDate());
    }

    @Test
    void renewShouldRequireANewEndDate() {
        when(repository.findById(4L)).thenReturn(Optional.of(activeContract(LocalDate.now().plusDays(1))));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.renew(4L, dto(4L)));

        assertEquals("New end date must be provided in the DTO for renewal.", e.getMessage());
    }

    @Test
    void renewShouldFailWhenContractIsMissing() {
        when(repository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.renew(4L, dto(4L)));
    }
}
