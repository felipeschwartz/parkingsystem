package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.PlanMapper;
import com.github.felipeschwartz.parkingsystem.mapper.PlanRateMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanRateDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.repository.PlanRateRepository;
import com.github.felipeschwartz.parkingsystem.repository.PlanRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanRateService  {

    private Logger logger = LoggerFactory.getLogger(PaymentService.class.getName());
    private final PlanRateRepository planRateRepository;
    private final PlanRateMapper planRateMapper;
    private final PlanMapper planMapper;
    private final PlanRepository planRepository;

    public PlanRateService(PlanRateRepository planRateRepository, PlanRateMapper planRateMapper, PlanMapper planMapper, PlanRepository planRepository) {
        this.planRateRepository = planRateRepository;
        this.planRateMapper = planRateMapper;
        this.planMapper = planMapper;
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public List<PlanRateDTO> findAll() {
        logger.info("Finding all plan rate records");
        return planRateRepository.findAll().stream().map(planRate -> planRateMapper.toDTO(planRate))
                .collect(Collectors.toList());
    }

    @Transactional
    public PlanRateDTO findById(Long id) {
        logger.info("Finding plan rate record with id {}", id);
        PlanRate rate = planRateRepository.findById(id).orElseThrow(() ->new ObjectNotFoundException("Plan rate not found with id: ", id));
        return planRateMapper.toDTO(rate);
    }


    @Transactional
    public PlanRateDTO create(PlanRateDTO planRateDTO) {
        logger.info("Creating plan rate record {}", planRateDTO);
        Plan plan = planRepository.findById(planRateDTO.getpId())
                .orElseThrow(() -> new ObjectNotFoundException("Plan not found with id: " + planRateDTO.getpId()));
        PlanRate planRate = planRateMapper.toEntity(planRateDTO);
        planRate.setVehicleType(planRateDTO.getVehicleType());
        planRate.setDurationMonths(planRateDTO.getDurationMonths());
        planRate.setMonthlyPrice(planRateDTO.getMonthlyPrice());
        planRate.setDiscountPercent(planRateDTO.getDiscountPercent());
        planRate.setActive(planRateDTO.getActive());
        planRate.setPlan(plan);
        planRate = planRateRepository.save(planRate);
        PlanRateDTO createdPlanRateDTO = planRateMapper.toDTO(planRate);
        createdPlanRateDTO.setId(planRate.getId());
        createdPlanRateDTO.setVehicleType(planRate.getVehicleType());
        createdPlanRateDTO.setDurationMonths(planRate.getDurationMonths());
        createdPlanRateDTO.setMonthlyPrice(planRate.getMonthlyPrice());
        createdPlanRateDTO.setDiscountPercent(planRate.getDiscountPercent());
        createdPlanRateDTO.setActive(planRate.getActive());
        createdPlanRateDTO.setCreatedAt(planRate.getCreatedAt());
        createdPlanRateDTO.setUpdatedAt(planRate.getUpdatedAt());
        createdPlanRateDTO.setpId(plan.getId());

        return createdPlanRateDTO;
    }


    @Transactional
    public PlanRateDTO update(PlanRateDTO updated) {
        logger.info("Updating plan rate record {}", updated);
        PlanRate existingRate = planRateRepository.findById(updated.getId())
                .orElseThrow(() ->new ObjectNotFoundException("Plan rate not found with id: ", updated.getId()));
        if (updated.getpId() != null) {
            Plan newPlan = planRepository.findPlanById(updated.getpId())
                    .orElseThrow(() ->new ObjectNotFoundException("Plan not found with id: " + updated.getpId()));
            existingRate.setPlan(newPlan);
        }
        if (updated.getVehicleType() != null) existingRate.setVehicleType(updated.getVehicleType());
        if (updated.getDurationMonths() != null) existingRate.setDurationMonths(updated.getDurationMonths());
        if (updated.getMonthlyPrice() != null) existingRate.setMonthlyPrice(updated.getMonthlyPrice());
        if (updated.getDiscountPercent() != null) existingRate.setDiscountPercent(updated.getDiscountPercent());
        if (updated.getActive() != null) existingRate.setActive(updated.getActive());
        existingRate.setUpdatedAt(LocalDateTime.now());
        PlanRate updatedRate = planRateRepository.save(existingRate);
        return planRateMapper.toDTO(updatedRate);
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting plan rate record with id {}", id);
        PlanRate rate = planRateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PlanRate not found: " + id));
        planRateRepository.delete(rate);
    }

    @Transactional
    public void deactivateRate(Long rateId) {
        PlanRate rate = planRateRepository.findById(rateId)
                .orElseThrow(() -> new IllegalArgumentException("PlanRate not found: " + rateId));

        rate.setActive(false);
        rate.setUpdatedAt(LocalDateTime.now());
        planRateRepository.save(rate);
    }
}
