package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.controller.HourlyRateController;
import com.github.felipeschwartz.parkingsystem.mapper.HourlyRateMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.repository.HourlyRateRepository;
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
public class HourlyRateService {

    private Logger logger = LoggerFactory.getLogger(VehicleService.class.getName());
    private final HourlyRateRepository hourlyRateRepository;
    private final HourlyRateMapper hourlyRateMapper; 

    public HourlyRateService(HourlyRateRepository hourlyRateRepository, HourlyRateMapper hourlyRateMapper) { 
        this.hourlyRateRepository = hourlyRateRepository;
        this.hourlyRateMapper = hourlyRateMapper;
    }

    @Transactional(readOnly = true) // Adicionado
    public List<HourlyRateDTO> findAll() {
        logger.info("Finding all Hourly Rates");
        List<HourlyRateDTO> hourlyRates = hourlyRateRepository.findAll().stream()
                .map(hourlyRateMapper::toDTO)
                .collect(Collectors.toList());
        hourlyRates.forEach(this::addHateoasLinks);
        return hourlyRates;
    }

    @Transactional(readOnly = true)
    public HourlyRateDTO findById(Long id) {
        logger.info("Finding Hourly Rate by ID {}", id);
        HourlyRate hourlyRate = hourlyRateRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("HourlyRate", id));
        HourlyRateDTO hourlyRateDTO = hourlyRateMapper.toDTO(hourlyRate);
        addHateoasLinks(hourlyRateDTO);
        return hourlyRateDTO;
    }

    @Transactional
    public HourlyRateDTO create(HourlyRateDTO hourlyRateDTO) {
        logger.info("Creating Hourly Rate {}", hourlyRateDTO);
        HourlyRate hourlyRate = hourlyRateMapper.toEntity(hourlyRateDTO);
        LocalDateTime now = LocalDateTime.now();
        hourlyRate.setCreatedAt(now);
        hourlyRate.setUpdatedAt(now);
        HourlyRateDTO createdHourlyRateDTO = hourlyRateMapper.toDTO(hourlyRateRepository.save(hourlyRate));
        addHateoasLinks(createdHourlyRateDTO);
        return createdHourlyRateDTO;
    }

    @Transactional
    public HourlyRateDTO update(HourlyRateDTO hourlyRateDTO) {
        logger.info("Updating Hourly Rate {}", hourlyRateDTO);
        HourlyRate rate = hourlyRateRepository.findById(hourlyRateDTO.getId())
                .orElseThrow(() -> new ObjectNotFoundException("HourlyRate", hourlyRateDTO.getId()));

        if (hourlyRateDTO.getVehicleType() != null) rate.setVehicleType(hourlyRateDTO.getVehicleType());
        if (hourlyRateDTO.getRatePerHour() != null) rate.setRatePerHour(hourlyRateDTO.getRatePerHour());
        if (hourlyRateDTO.getActive() != null) rate.setActive(hourlyRateDTO.getActive());
        rate.setUpdatedAt(LocalDateTime.now());
        HourlyRateDTO updatedHourlyRateDTO = hourlyRateMapper.toDTO(hourlyRateRepository.save(rate));
        addHateoasLinks(updatedHourlyRateDTO);
        return updatedHourlyRateDTO;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting Hourly Rate {}", id);
        HourlyRate hourlyRate = hourlyRateRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("HourlyRate", id));

        hourlyRateRepository.delete(hourlyRate);
    }


    private void addHateoasLinks(HourlyRateDTO dto) {
        dto.add(linkTo(methodOn(HourlyRateController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(HourlyRateController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(HourlyRateController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(HourlyRateController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(HourlyRateController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}