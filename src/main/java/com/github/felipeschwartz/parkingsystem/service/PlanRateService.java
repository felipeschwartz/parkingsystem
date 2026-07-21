package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.controller.PlanRateController;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<PlanRateDTO> planRates = planRateRepository.findAll().stream()
                .map(planRateMapper::toDTO).collect(Collectors.toList());
        planRates.forEach(this::addHateoasLinks);
        return planRates;
    }

    @Transactional
    public PlanRateDTO findById(Long id) {
        logger.info("Finding plan rate record with id {}", id);
        PlanRate planRate = planRateRepository.findById(id)
                .orElseThrow(() ->new ObjectNotFoundException("Plan rate not found with id: ", id));
        PlanRateDTO planRateDTO = planRateMapper.toDTO(planRate);
        addHateoasLinks(planRateDTO);
        return planRateDTO;
    }

    @Transactional
    public PlanRateDTO create(PlanRateDTO planRateDTO) {
        logger.info("Creating plan rate record {}", planRateDTO);
        PlanRate planRate = planRateMapper.toEntity(planRateDTO);
        planRate.setCreatedAt(LocalDateTime.now());
        planRate.setUpdatedAt(LocalDateTime.now());
        PlanRateDTO planRateDTOCreated = planRateMapper.toDTO(planRateRepository.save(planRate));
        return planRateDTOCreated;
    }


    @Transactional
    public PlanRateDTO update(PlanRateDTO updatedDto) {
        logger.info("Updating plan rate record {}", updatedDto);
        PlanRate existingRate = planRateRepository.findById(updatedDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Plan rate not found with id: " + updatedDto.getId()));
//        if (updatedDto.getpId() != null) {
//            Plan newPlan = planRepository.findById(updatedDto.getpId())
//                    .orElseThrow(() -> new ObjectNotFoundException("Plan not found with id: " + updatedDto.getpId()));
//            existingRate.setPlan(newPlan);
//        }
        planRateMapper.updatePlanRateFromDto(updatedDto, existingRate);
        existingRate.setUpdatedAt(LocalDateTime.now());
        PlanRateDTO planRateDTO = planRateMapper.toDTO(planRateRepository.save(existingRate));
        addHateoasLinks(planRateDTO);
        return planRateDTO;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting plan rate record with id {}", id);
        if (!planRateRepository.existsById(id)) {
            throw new ObjectNotFoundException("Plan rate not found with id: " + id);
        }
        planRateRepository.deleteById(id);
    }

    @Transactional
    public void deactivateRate(Long rateId) {
        PlanRate rate = planRateRepository.findById(rateId)
                .orElseThrow(() -> new IllegalArgumentException("PlanRate not found: " + rateId));

        rate.setActive(false);
        rate.setUpdatedAt(LocalDateTime.now());
        planRateRepository.save(rate);
    }

    private void addHateoasLinks(PlanRateDTO dto) {
        dto.add(linkTo(methodOn(PlanRateController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PlanRateController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PlanRateController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PlanRateController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PlanRateController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
