package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.SubscriptionContract;
import parkingsystem.felipeschwartz.com.github.model.enums.SubscripionStatus;
import parkingsystem.felipeschwartz.com.github.repositories.SubscriptionContractRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionContractService {

    private final SubscriptionContractRepository subscriptionContractRepository;

    public SubscriptionContractService(SubscriptionContractRepository subscriptionContractRepository) {
        this.subscriptionContractRepository = subscriptionContractRepository;
    }

    @Transactional(readOnly = true)
    public SubscriptionContract findActiveContractOnDate(Long veicleId, LocalDate date) {
        return subscriptionContractRepository.findActiveOnDate(veicleId, SubscripionStatus.ACTIVE, date).orElse(null);
    }

    @Transactional
    public SubscriptionContract createContract(SubscriptionContract contract) {
        validateDates(contract.getStartDate(), contract.getEndDate());

        // Se endDate for null (sem fim), use uma data "bem grande" só para checar sobreposição
        LocalDate end = (contract.getEndDate() == null) ? LocalDate.of(9999, 12, 31) : contract.getEndDate();

        List<SubscriptionContract> overlaps = subscriptionContractRepository.findOverlappingContracts(
                contract.getVehicle().getId(),
                SubscripionStatus.ACTIVE,
                contract.getStartDate(),
                end
        );

        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("Já existe contrato ATIVO com período sobreposto para este veículo.");
        }

        // Garante status default
        if (contract.getStatus() == null) {
            contract.setStatus(SubscripionStatus.ACTIVE);
        }

        return subscriptionContractRepository.save(contract);
    }


    @Transactional
    public SubscriptionContract cancel(Long contractId) {
        SubscriptionContract contract = subscriptionContractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado: " + contractId));

        contract.setStatus(SubscripionStatus.CANCELLED);
        return subscriptionContractRepository.save(contract);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate não pode ser nulo.");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate não pode ser anterior a startDate.");
        }
    }

}
