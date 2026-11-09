package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.CycleAvoidingMappingContext;
import com.github.felipeschwartz.parkingsystem.mapper.PlanMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanCreationDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import com.github.felipeschwartz.parkingsystem.repository.PlanRateRepository;
import com.github.felipeschwartz.parkingsystem.repository.PlanRepository;
import com.github.felipeschwartz.parkingsystem.repository.SubscriptionContractRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlanService {
    private Logger logger = LoggerFactory.getLogger(PaymentService.class.getName());

    private final PlanMapper planMapper;
    private final PlanRepository planRepository;
    private final CycleAvoidingMappingContext context;
    private final PlanRateRepository planRateRepository;
    private final SubscriptionContractRepository subscriptionContractRepository;

    public PlanService(PlanMapper planMapper, PlanRepository planRepository,
                       CycleAvoidingMappingContext context, PlanRateRepository planRateRepository,
                       SubscriptionContractRepository subscriptionContractRepository) {
        this.planMapper = planMapper;
        this.planRepository = planRepository;
        this.context = context;
        this.planRateRepository = planRateRepository;
        this.subscriptionContractRepository = subscriptionContractRepository;
    }

    @Transactional(readOnly = true)
    public List<PlanDTO> findAll() {
        logger.info("Finding all plan records");
        List<PlanDTO> planDTOS = planRepository.findAll().stream()
                .map(entity -> planMapper.toDTO(entity, context))
                .collect(Collectors.toList());
        return planDTOS;
    }

    @Transactional(readOnly = true)
    public PlanDTO findById(Long id) {
        logger.info("Finding plan record with id {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Plan", id));
        PlanDTO planDTO = planMapper.toDTO(plan, context);
        return planDTO;
    }

    @Transactional
    public PlanDTO create(PlanCreationDTO planCreationDTO) {
        logger.info("Creating plan with name {}", planCreationDTO.name());
        Plan planToSave = planMapper.toEntity(planCreationDTO);
        planToSave.setCreatedAt(LocalDateTime.now());
        planToSave.setUpdatedAt(LocalDateTime.now());
        Plan savedPlan = planRepository.save(planToSave);
        if (planCreationDTO.rateIds() != null && !planCreationDTO.rateIds().isEmpty()) {
            Set<PlanRate> fetchedRates = new HashSet<>(planRateRepository.findAllById(planCreationDTO.rateIds()));
            for (PlanRate planRate : fetchedRates) {
                planRate.setPlan(savedPlan);
                savedPlan.getRates().add(planRate);
            }
        }
        if (planCreationDTO.subscriptionContractIds() != null && !planCreationDTO.subscriptionContractIds().isEmpty()) {
            Set<SubscriptionContract> fetchedContracts = new HashSet<>(subscriptionContractRepository.findAllById(planCreationDTO
                    .subscriptionContractIds()));
            for (SubscriptionContract subscriptionContract : fetchedContracts) {
                subscriptionContract.setPlan(savedPlan);
                savedPlan.getSubscriptionContracts().add(subscriptionContract);
            }
        }
        savedPlan.validate();
        PlanDTO savedPlanDTO = planMapper.toDTO(savedPlan, context);
        return savedPlanDTO;
    }

    @Transactional
    public PlanDTO update(PlanDTO updated) {
        logger.info("Updating plan with id {}", updated.id());
        Plan existingPlan = planRepository.findById(updated.id())
                .orElseThrow(() -> new ObjectNotFoundException("Plan", updated.id()));
        planMapper.updateEntityFromDTO(updated, existingPlan);
        existingPlan.setUpdatedAt(LocalDateTime.now());
        PlanDTO updatedPlanDTO = planMapper.toDTO(existingPlan, context);
        return updatedPlanDTO;
    }

    @Transactional
    public void activatePlan(Long id) {
        logger.info("Activating plan with id {}", id);
        Plan p = planRepository.findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("Plan", id));
        p.setActive(true);
        p.setUpdatedAt(LocalDateTime.now());
        planRepository.save(p);
    }

    @Transactional
    public void deactivatePlan(Long id) {
        logger.info("Deactivating plan with id {}", id);
        Plan p = planRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Plan", id));
        p.setActive(false);
        p.setUpdatedAt(LocalDateTime.now());
        planRepository.save(p);
    }

    @Transactional
    public void deletePlan(Long id) {
        logger.info("Deleting plan with id {}", id);
        if (!planRepository.existsById(id)) {
            throw new ObjectNotFoundException("Plan", id);
        }
        planRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Plan getPlanOrThrow(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
    }

}
