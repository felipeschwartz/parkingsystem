package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.model.dto.*;
import com.github.felipeschwartz.parkingsystem.model.entity.*;
import com.github.felipeschwartz.parkingsystem.model.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercises the generated MapStruct implementations against the record DTOs, which the service tests
 * cannot do because they mock the mappers.
 */
@SpringJUnitConfig(RecordMappersTest.Scan.class)
class RecordMappersTest {

    @Configuration
    @ComponentScan(basePackageClasses = AddressMapper.class)
    static class Scan {
    }

    @Autowired HourlyRateMapper hourlyRateMapper;
    @Autowired UserSummaryMapper userSummaryMapper;
    @Autowired VehicleMapper vehicleMapper;
    @Autowired UserIndividualMapper individualMapper;
    @Autowired UserEntityMapper entityMapper;
    @Autowired UserCreationMapper creationMapper;
    @Autowired PlanMapper planMapper;
    @Autowired PlanRateMapper planRateMapper;
    @Autowired SubscriptionContractMapper contractMapper;
    @Autowired ReservationMapper reservationMapper;
    @Autowired ParkingSpaceMapper spaceMapper;
    @Autowired ParkingLotMapper lotMapper;
    @Autowired CycleAvoidingMappingContext context;

    private static final VehicleType TYPE = VehicleType.values()[0];

    private UserIndividual person() {
        UserIndividual user = new UserIndividual();
        user.setId(5L);
        user.setEmail("ana@x.com");
        user.setPhone("119");
        user.setUserType(UserType.INDIVIDUAL);
        user.setRoles(Set.of("ROLE_USER"));
        user.setCpf("12345678901");
        user.setFirstName("Ana");
        return user;
    }

    private Vehicle vehicle(User owner) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(2L);
        vehicle.setLicensePlate("ABC1234");
        vehicle.setType(TYPE);
        vehicle.setUser(owner);
        return vehicle;
    }

    @Test
    void theSpringManagedContextShouldBeWiredWithTheMappers() {
        assertNotNull(context.getPlanMapper());
        assertNotNull(context.getPlanRateMapper());
        assertNotNull(context.getSubscriptionContractMapper());
    }

    @Test
    void hourlyRateShouldMapBothWays() {
        HourlyRate entity = new HourlyRate(TYPE, new BigDecimal("5.00"), true);
        entity.setId(1L);

        HourlyRateDTO dto = hourlyRateMapper.toDTO(entity);

        assertEquals(1L, dto.id());
        assertEquals(TYPE, dto.vehicleType());
        assertEquals(true, dto.active());
        assertEquals(new BigDecimal("5.00"), dto.ratePerHour());
        HourlyRate back = hourlyRateMapper.toEntity(dto);
        assertNull(back.getId());
        assertEquals(new BigDecimal("5.00"), back.getRatePerHour());
    }

    @Test
    void userSummaryShouldCarryCpfOrCnpjDependingOnTheUserType() {
        UserSummaryDTO individual = userSummaryMapper.userToSummaryDTO(person());
        UserEntity company = new UserEntity();
        company.setCnpj("99888777000166");
        company.setUserType(UserType.ENTITY);

        assertEquals("12345678901", individual.cpf());
        assertNull(individual.cnpj());
        assertEquals("ana@x.com", individual.email());
        assertEquals(Set.of("ROLE_USER"), individual.roles());
        assertEquals("99888777000166", userSummaryMapper.userToSummaryDTO(company).cnpj());
        assertNull(userSummaryMapper.userToSummaryDTO(null));
    }

    @Test
    void vehicleShouldMapOwnerSummaryAndLeaveCollectionsEmpty() {
        VehicleDTO dto = vehicleMapper.toDTO(vehicle(person()));

        assertEquals("ABC1234", dto.licensePlate());
        assertEquals(TYPE, dto.type());
        assertEquals("12345678901", dto.user().cpf());
        assertTrue(dto.subscriptionContracts().isEmpty());
        assertTrue(dto.sessions().isEmpty());
    }

    @Test
    void individualUserShouldMapAddressAndVehicles() {
        UserIndividual user = person();
        user.setAddress(new Address("Rua A", "10", null, "SP", "SP", "01000", "BR"));
        user.setVehicles(new HashSet<>(Set.of(vehicle(user))));

        UserIndividualDTO dto = individualMapper.toDTO(user);

        assertEquals("12345678901", dto.cpf());
        assertEquals("Ana", dto.firstName());
        assertEquals("Rua A", dto.address().street());
        assertEquals(1, dto.vehicles().size());
        assertEquals("ABC1234", dto.vehicles().iterator().next().licensePlate());
    }

    @Test
    void entityUserShouldMap() {
        UserEntity user = new UserEntity();
        user.setId(8L);
        user.setCnpj("99888777000166");
        user.setCorporateName("ACME");

        UserEntityDTO dto = entityMapper.toDTO(user);

        assertEquals("99888777000166", dto.cnpj());
        assertEquals("ACME", dto.corporateName());
    }

    @Test
    void creationRequestShouldBuildAnIndividualWithItsVehicles() {
        VehicleDTO vehicle = TestDtos.vehicle(null, "XYZ9876");
        CreateUserRequestDTO request = new CreateUserRequestDTO("119", "ana@x.com", "secret", null,
                UserType.INDIVIDUAL, UserProfile.USER, "12345678901", "Ana", "Souza", LocalDate.of(1990, 1, 1),
                null, null, null, null, Set.of(vehicle));

        UserIndividual user = creationMapper.toIndividualEntity(request);

        assertEquals("12345678901", user.getCpf());
        assertEquals("Souza", user.getLastName());
        assertEquals("ana@x.com", user.getEmail());
        assertEquals(1, user.getVehicles().size());
        assertEquals("XYZ9876", user.getVehicles().iterator().next().getLicensePlate());
    }

    @Test
    void creationRequestShouldPickTheEntityTypeFromTheUserType() {
        CreateUserRequestDTO request = TestDtos.createUserRequest(UserType.ENTITY, null, "99888777000166");

        User user = creationMapper.toEntity(request);

        assertInstanceOf(UserEntity.class, user);
    }

    @Test
    void updateShouldApplyTheRequestOnTheExistingIndividual() {
        UserIndividual existing = person();
        CreateUserRequestDTO request = new CreateUserRequestDTO("118", "new@x.com", "secret", null,
                UserType.INDIVIDUAL, UserProfile.USER, "000", "Bia", "Lima", null, null, null, null, null, null);

        individualMapper.updateIndividualFromCreateRequest(request, existing);

        assertEquals("000", existing.getCpf());
        assertEquals("Bia", existing.getFirstName());
        assertEquals(5L, existing.getId());
    }

    @Test
    void planShouldMapRatesAndContracts() {
        Plan plan = new Plan(1L, "Gold");
        PlanRate rate = new PlanRate();
        rate.setId(3L);
        rate.setPlan(plan);
        rate.setVehicleType(TYPE);
        rate.setMonthlyPrice(new BigDecimal("100.00"));
        rate.setActive(true);
        plan.setRates(new HashSet<>(Set.of(rate)));
        SubscriptionContract contract = new SubscriptionContract();
        contract.setId(4L);
        contract.setPlan(plan);
        contract.setStatus(SubscripionStatus.ACTIVE);
        contract.setStartDate(LocalDate.of(2026, 1, 1));
        contract.setVehicle(vehicle(person()));
        contract.setUser(person());
        plan.setSubscriptionContracts(new HashSet<>(Set.of(contract)));

        PlanDTO dto = planMapper.toDTO(plan, context);

        assertEquals("Gold", dto.name());
        assertEquals(1, dto.rates().size());
        assertEquals(new BigDecimal("100.00"), dto.rates().iterator().next().monthlyPrice());
        assertEquals(1, dto.subscriptionContracts().size());
        SubscriptionContractDTO mapped = dto.subscriptionContracts().iterator().next();
        assertEquals(SubscripionStatus.ACTIVE, mapped.status());
        assertEquals("ABC1234", mapped.vehicle().licensePlate());
        assertEquals("12345678901", mapped.user().cpf());
    }

    @Test
    void subscriptionContractShouldMapPlanVehicleAndUserSummaries() {
        Plan plan = new Plan(1L, "Gold");
        SubscriptionContract contract = new SubscriptionContract();
        contract.setId(4L);
        contract.setPlan(plan);
        contract.setVehicle(vehicle(person()));
        contract.setUser(person());
        contract.setStatus(SubscripionStatus.ACTIVE);

        SubscriptionContractDTO dto = contractMapper.toDTO(contract, context);

        assertEquals(4L, dto.id());
        assertEquals("Gold", dto.plan().name());
        assertEquals("ABC1234", dto.vehicle().licensePlate());
        assertEquals("ana@x.com", dto.user().email());
    }

    @Test
    void planRateUpdateShouldIgnoreNullsAndKeepTheId() {
        PlanRate existing = new PlanRate();
        existing.setId(3L);
        existing.setVehicleType(TYPE);
        existing.setMonthlyPrice(new BigDecimal("100.00"));
        PlanRateDTO patch = new PlanRateDTO(99L, null, null, null, null, 6, null, null, null, null);

        planRateMapper.updatePlanRateFromDto(patch, existing);

        assertEquals(3L, existing.getId());
        assertEquals(6, existing.getDurationMonths());
        assertEquals(new BigDecimal("100.00"), existing.getMonthlyPrice());
        assertEquals(TYPE, existing.getVehicleType());
    }

    @Test
    void reservationShouldMapAndPatchIgnoringNulls() {
        Reservation reservation = new Reservation();
        reservation.setId(6L);
        reservation.setVehicle(vehicle(person()));
        reservation.setStatus(ReservationStatus.values()[0]);
        reservation.setStartTime(LocalDateTime.of(2026, 1, 1, 10, 0));

        ReservationDTO dto = reservationMapper.toDTO(reservation);

        assertEquals(6L, dto.id());
        assertEquals(LocalDateTime.of(2026, 1, 1, 10, 0), dto.startTime());

        ReservationDTO patch = new ReservationDTO(null, null, null, null, null, LocalDateTime.of(2026, 1, 1, 12, 0), null, null, null);
        reservationMapper.updateReservationFromDto(patch, reservation);
        assertEquals(LocalDateTime.of(2026, 1, 1, 12, 0), reservation.getEndTime());
        assertEquals(LocalDateTime.of(2026, 1, 1, 10, 0), reservation.getStartTime());
    }

    @Test
    void parkingLotShouldMapAddressAndSpaces() {
        ParkingLot lot = new ParkingLot();
        lot.setId(1L);
        lot.setParkingLotName("Central");
        lot.setAddress(new Address("Rua B", "1", null, "SP", "SP", "01000", "BR"));

        ParkingLotDTO dto = lotMapper.toDTO(lot);

        assertEquals("Central", dto.parkingLotName());
        assertEquals("Rua B", dto.address().street());
        assertTrue(dto.parkingSpaces().isEmpty());
    }

    @Test
    void parkingSpaceShouldMapItsLotSummary() {
        ParkingLot lot = new ParkingLot();
        lot.setId(1L);
        lot.setParkingLotName("Central");
        ParkingSpace space = new ParkingSpace();
        space.setId(9L);
        space.setFloor("1");
        space.setParkingLot(lot);

        ParkingSpaceDTO dto = spaceMapper.toDTO(space);

        assertEquals("1", dto.floor());
        assertEquals("Central", dto.parkingLot().parkingLotName());
    }
}
