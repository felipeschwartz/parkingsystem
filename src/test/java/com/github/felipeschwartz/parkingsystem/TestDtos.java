package com.github.felipeschwartz.parkingsystem;

import com.github.felipeschwartz.parkingsystem.model.dto.*;
import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;

import java.time.LocalDate;
import java.util.Set;

/**
 * Builders for the response/request records. They fill only what a test needs and leave everything else null.
 */
public final class TestDtos {

    private TestDtos() {
    }

    public static HourlyRateDTO hourlyRate(Long id) {
        return new HourlyRateDTO(id, null, null, null, null, null);
    }

    public static ParkingLotDTO parkingLot(Long id) {
        return new ParkingLotDTO(id, null, null, null, null, null, null, null, null, null, null, null);
    }

    public static ParkingSpaceDTO parkingSpace(Long id) {
        return new ParkingSpaceDTO(id, null, null, null, null, null, null, null, null);
    }

    public static PaymentDTO payment(Long id) {
        return new PaymentDTO(id, null, null, null, null, null, null, null, null);
    }

    public static PlanDTO plan(Long id) {
        return new PlanDTO(id, null, null, null, null, null, null);
    }

    public static PlanRateDTO planRate(Long id) {
        return new PlanRateDTO(id, null, null, null, null, null, null, null, null, null);
    }

    public static ReservationDTO reservation(Long id) {
        return reservation(id, null, null);
    }

    public static ReservationDTO reservation(Long id, Long vehicleId, Long parkingSpaceId) {
        return new ReservationDTO(id, null, vehicleId, parkingSpaceId == null ? null : parkingSpace(parkingSpaceId),
                null, null, null, null, null);
    }

    public static SubscriptionContractDTO subscriptionContract(Long id) {
        return new SubscriptionContractDTO(id, null, null, null, null, null, null, null, null, null);
    }

    public static SubscriptionContractDTO subscriptionContract(Long planId, Long vehicleId, Long userId,
                                                               LocalDate start, LocalDate end) {
        VehicleSummaryDTO vehicle = new VehicleSummaryDTO(vehicleId, null, null);
        UserSummaryDTO user = new UserSummaryDTO(userId, null, null, null, null, null, null, null);
        return new SubscriptionContractDTO(null, vehicle, null, planId, start, end, null, user, null, null);
    }

    public static VehicleDTO vehicle(Long id, String licensePlate) {
        return new VehicleDTO(id, licensePlate, null, null, null, null, null, null);
    }

    public static ParkingSessionDTO parkingSession(Long id) {
        return new ParkingSessionDTO(id, null, null, null, null, null, null, null, null, null, null, null);
    }

    public static UserIndividualDTO userIndividual(Long id, String cpf) {
        return new UserIndividualDTO(id, null, null, null, null, null, null, null, null, cpf, null, null, null);
    }

    public static UserEntityDTO userEntity(Long id, String cnpj) {
        return new UserEntityDTO(id, null, null, null, null, null, null, null, null, cnpj, null, null, null);
    }

    public static CreateUserRequestDTO createUserRequest(UserType type, String cpf, String cnpj) {
        return createUserRequest(type, UserProfile.ADMIN, cpf, cnpj, Set.of());
    }

    public static CreateUserRequestDTO createUserRequest(UserType type, UserProfile profile, String cpf, String cnpj,
                                                         Set<VehicleDTO> vehicles) {
        return new CreateUserRequestDTO(null, null, "secret", null, type, profile, cpf, null, null, null,
                cnpj, null, null, null, vehicles);
    }
}
