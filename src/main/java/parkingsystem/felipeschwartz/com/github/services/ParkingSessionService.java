package parkingsystem.felipeschwartz.com.github.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.HourlyRate;
import parkingsystem.felipeschwartz.com.github.model.entities.ParkingSession;
import parkingsystem.felipeschwartz.com.github.model.entities.ParkingSpace;
import parkingsystem.felipeschwartz.com.github.model.entities.Vehicle;
import parkingsystem.felipeschwartz.com.github.model.enums.SessionStatus;
import parkingsystem.felipeschwartz.com.github.model.enums.SpaceStatus;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import parkingsystem.felipeschwartz.com.github.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ParkingSessionService {
    private final ParkingSessionRepository sessionRepository;
    private final ParkingSpaceRepository spaceRepository;
    private final VehicleRepository vehicleRepository;
    private final HourlyRateRepository hourlyRateRepository;
    private final SubscriptionContractRepository subscriptionContractRepository;

    public ParkingSessionService(
            ParkingSessionRepository sessionRepository,
            ParkingSpaceRepository spaceRepository,
            VehicleRepository vehicleRepository,
            HourlyRateRepository hourlyRateRepository,
            SubscriptionContractRepository subscriptionContractRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.spaceRepository = spaceRepository;
        this.vehicleRepository = vehicleRepository;
        this.hourlyRateRepository = hourlyRateRepository;
        this.subscriptionContractRepository = subscriptionContractRepository;
    }

    // -------------------------
    // OPEN - HOURLY (avulso)
    // -------------------------
    @Transactional
    public ParkingSession openHourlySession(Long parkingSpaceId,
                                            String licensePlate,
                                            VehicleType vehicleType,
                                            LocalDateTime entryTime) {

        LocalDateTime when = (entryTime != null) ? entryTime : LocalDateTime.now();

        ParkingSpace space = spaceRepository.findByIdForUpdate(parkingSpaceId)
                .orElseThrow(() -> new IllegalArgumentException("ParkingSpace não encontrado: " + parkingSpaceId));

        validateSpaceCanBeUsed(space, vehicleType);

        // garante 1 OPEN por vaga (além do lock)
        if (sessionRepository.existsByParkingSpace_IdAndStatus(space.getId(), SessionStatus.OPEN)) {
            throw new IllegalStateException("Já existe uma sessão OPEN para esta vaga.");
        }

        ParkingSession session = ParkingSession.forHourly(licensePlate, vehicleType, space, when);

        // marca vaga como ocupada (se você usa status)
        space.setStatus(SpaceStatus.OCCUPIED);

        // salva
        spaceRepository.save(space);
        return sessionRepository.save(session);
    }

    // -------------------------
    // OPEN - SUBSCRIPTION (mensal)
    // -------------------------
    @Transactional
    public ParkingSession openSubscriptionSession(Long parkingSpaceId,
                                                  Long vehicleId,
                                                  LocalDateTime entryTime) {

        LocalDateTime when = (entryTime != null) ? entryTime : LocalDateTime.now();

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle não encontrado: " + vehicleId));

        ParkingSpace space = spaceRepository.findByIdForUpdate(parkingSpaceId)
                .orElseThrow(() -> new IllegalArgumentException("ParkingSpace não encontrado: " + parkingSpaceId));

        validateSpaceCanBeUsed(space, vehicle.getType());

        if (sessionRepository.existsByParkingSpace_IdAndStatus(space.getId(), SessionStatus.OPEN)) {
            throw new IllegalStateException("Já existe uma sessão OPEN para esta vaga.");
        }

        // opcional: bloquear se não tiver contrato ativo (se esta for sua regra)
        boolean hasActiveContract = subscriptionContractRepository.hasActiveContractForDate(
                vehicle.getId(), when.toLocalDate()
        );
        if (!hasActiveContract) {
            throw new IllegalStateException("Veículo não possui contrato mensal ativo para hoje.");
        }

        ParkingSession session = ParkingSession.forSubscription(vehicle, space, when);

        space.setStatus(SpaceStatus.OCCUPIED);

        spaceRepository.save(space);
        return sessionRepository.save(session);
    }

    // -------------------------
    // CLOSE
    // -------------------------
    @Transactional
    public ParkingSession closeSession(Long sessionId, LocalDateTime exitTime) {
        LocalDateTime when = (exitTime != null) ? exitTime : LocalDateTime.now();

        ParkingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("ParkingSession não encontrada: " + sessionId));

        if (session.getStatus() != SessionStatus.OPEN) {
            throw new IllegalStateException("Só é possível fechar sessão OPEN.");
        }

        session.close(when);

        // cobrança:
        // - se for mensal com contrato ativo na data, cobra 0
        // - senão cobra por hora
        BigDecimal amount = calculateAmountFor(session);
        session.setAmountCharged(amount);

        // libera vaga
        ParkingSpace space = session.getParkingSpace();
        if (space != null) {
            // aqui seria interessante travar a vaga também; mas normalmente não precisa no close
            space.setStatus(SpaceStatus.AVALIABLE);
            spaceRepository.save(space);
        }

        return sessionRepository.save(session);
    }

    private BigDecimal calculateAmountFor(ParkingSession session) {
        // Se tem Vehicle e contrato ativo no dia de entrada, considera mensal (isento)
        if (session.getVehicle() != null) {
            LocalDate day = session.getEntryTime().toLocalDate();
            boolean hasContract = subscriptionContractRepository.hasActiveContractForDate(
                    session.getVehicle().getId(), day
            );
            if (hasContract) {
                return BigDecimal.ZERO.setScale(2);
            }
        }

        // Hourly
        HourlyRate rate = hourlyRateRepository
                .findByVehicleTypeAndActiveTrue(session.getVehicleType())
                .orElseThrow(() -> new IllegalStateException("Não existe HourlyRate ativo para " + session.getVehicleType()));

        return session.calculateAmount(rate.getRatePerHour());
    }

    private void validateSpaceCanBeUsed(ParkingSpace space, VehicleType vehicleType) {
        if (!Boolean.TRUE.equals(space.getActive())) {
            throw new IllegalStateException("Vaga inativa.");
        }
        if (space.getStatus() != SpaceStatus.AVALIABLE) {
            throw new IllegalStateException("Vaga não está AVAILABLE.");
        }
        if (space.getVehicleType() != vehicleType) {
            throw new IllegalStateException("Tipo de veículo não permitido nesta vaga.");
        }
    }
}
