package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.CycleAvoidingMappingContext;
import com.github.felipeschwartz.parkingsystem.mapper.PlanMapper;
import com.github.felipeschwartz.parkingsystem.mapper.PlanRateMapper;
import com.github.felipeschwartz.parkingsystem.mapper.SubscriptionContractMapper;
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

    private final PlanRepository planRepository;
    private final PlanMapper mapper;
    private final CycleAvoidingMappingContext context;
    private final PlanRateRepository planRateRepository;
    private final SubscriptionContractRepository subscriptionContractRepository;
    private final PlanRateMapper planRateMapper;
    private final SubscriptionContractMapper subscriptionContractMapper;

    public PlanService(PlanRepository planRepository, PlanMapper mapper, CycleAvoidingMappingContext context, PlanRateRepository planRateRepository, SubscriptionContractRepository subscriptionContractRepository, PlanRateMapper planRateMapper, SubscriptionContractMapper subscriptionContractMapper) {
        this.planRepository = planRepository;
        this.mapper = mapper;
        this.context = context;
        this.planRateRepository = planRateRepository;
        this.subscriptionContractRepository = subscriptionContractRepository;
        this.planRateMapper = planRateMapper;
        this.subscriptionContractMapper = subscriptionContractMapper;
    }

    //PLAN
    @Transactional(readOnly = true)
    public List<PlanDTO> findAll() {
        logger.info("Finding all plan records");
        return planRepository.findAll().stream().map(plan -> mapper.planToPlanDTO(plan, context))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlanDTO findById(Long id) {
        logger.info("Finding plan record with id {}", id);
        Plan plan = planRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Plan not found with id: ", id));
        return mapper.toDTO(plan, context);
    }

    @Transactional
    public PlanDTO create(PlanCreationDTO planCreationDTO) {
        logger.info("Creating plan with name {}", planCreationDTO.getName());
        Plan planToSave = mapper.toEntity(planCreationDTO);
        Plan savedPlan = planRepository.save(planToSave);
        if (planCreationDTO.getRateIds() != null && !planCreationDTO.getRateIds().isEmpty()) {
            Set<PlanRate> fetchedRates = new HashSet<>(planRateRepository.findAllById(planCreationDTO.getRateIds()));
            for (PlanRate planRate : fetchedRates) {
                planRate.setPlan(savedPlan);
                savedPlan.getRates().add(planRate);
            }
        }
        if (planCreationDTO.getSubscriptionContractIds() != null && !planCreationDTO.getSubscriptionContractIds().isEmpty()) {
            Set<SubscriptionContract> fetchedContracts = new HashSet<>(subscriptionContractRepository.findAllById(planCreationDTO.getSubscriptionContractIds()));
            for (SubscriptionContract subscriptionContract : fetchedContracts) {
                subscriptionContract.setPlan(savedPlan);
                savedPlan.getSubscriptionContracts().add(subscriptionContract);
            }
        }
        savedPlan.validate();
        return mapper.toDTO(savedPlan, new CycleAvoidingMappingContext(mapper, subscriptionContractMapper, planRateMapper));
    }


    @Transactional
    public PlanDTO update(PlanDTO updated) {
        logger.info("Updating plan with id {}", updated.getId());
        Plan existingPlan = planRepository.findById(updated.getId()).orElseThrow(() -> new ObjectNotFoundException("Plan not found with id: ", updated.getId()));
        if (updated.getName() != null) existingPlan.setName(updated.getName());
        if (updated.getActive() != null) existingPlan.setActive(updated.getActive());
        existingPlan.setUpdatedAt(LocalDateTime.now());
        Plan updatedPlan = planRepository.save(existingPlan);
        return mapper.toDTO(updatedPlan, context);
    }

    @Transactional
    public void activatePlan(Long id) {
        Plan p = getPlanOrThrow(id);
        p.setActive(true);
        p.setUpdatedAt(LocalDateTime.now());
        planRepository.save(p);
    }

    @Transactional
    public void deactivatePlan(Long id) {
        Plan p = getPlanOrThrow(id);
        p.setActive(false);
        p.setUpdatedAt(LocalDateTime.now());
        planRepository.save(p);
    }

    @Transactional
    public void deletePlan(Long id) {
        logger.info("Deleting plan with id {}", id);
        if (!planRepository.existsById(id)) {
            throw new ObjectNotFoundException("Plan not found with id: ", id);
        }
        planRepository.deleteById(id);
    }

    // --------------------
    // helpers
    // --------------------
    @Transactional(readOnly = true)
    public Plan getPlanOrThrow(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
    }


}
