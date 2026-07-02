package com.github.felipeschwartz.parkingsystem.service;
import com.github.felipeschwartz.parkingsystem.mapper.CycleAvoidingMappingContext;
import com.github.felipeschwartz.parkingsystem.mapper.PlanMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanCreationDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.PlanRateRepository;
import com.github.felipeschwartz.parkingsystem.repository.PlanRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PlanService {
    private Logger logger = LoggerFactory.getLogger(PaymentService.class.getName());

    private final PlanRepository planRepository;
    private final PlanMapper mapper;
    private final CycleAvoidingMappingContext context;

    public PlanService(PlanRepository planRepository, PlanMapper mapper, CycleAvoidingMappingContext context) {
        this.planRepository = planRepository;
        this.mapper = mapper;
        this.context = context;
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
        return mapper.toDTO(savedPlan, context);
    }

    @Transactional
    public PlanDTO update(Long id, PlanDTO updated) {
        logger.info("Updating plan with id {}", id);
        Plan existingPlan = planRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Plan not found with id: ", id));
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
