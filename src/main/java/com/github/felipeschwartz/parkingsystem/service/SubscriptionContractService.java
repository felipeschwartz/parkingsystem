package com.github.felipeschwartz.parkingsystem.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.repository.SubscriptionContractRepository;
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
            throw new IllegalStateException("There is already an active contract with an overlapping period for this vehicle..");
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
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + contractId));

        contract.setStatus(SubscripionStatus.CANCELLED);
        return subscriptionContractRepository.save(contract);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate cannot be null.");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be earlier than startDate.");
        }
    }

}
