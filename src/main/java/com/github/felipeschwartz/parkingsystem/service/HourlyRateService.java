package com.github.felipeschwartz.parkingsystem.service;

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

@Service
public class HourlyRateService {

    private Logger logger = LoggerFactory.getLogger(VehicleService.class.getName());
    private final HourlyRateRepository hourlyRateRepository;
    private final HourlyRateMapper hourlyRateMapper; 

    public HourlyRateService(HourlyRateRepository hourlyRateRepository, HourlyRateMapper hourlyRateMapper) { 
        this.hourlyRateRepository = hourlyRateRepository;
        this.hourlyRateMapper = hourlyRateMapper;
    }

    @Transactional(readOnly = true)
    public List<HourlyRateDTO> findAll() {
        logger.info("Finding all Hourly Rates");
        List<HourlyRateDTO> hourlyRates = hourlyRateRepository.findAll().stream()
                .map(hourlyRateMapper::toDTO)
                .collect(Collectors.toList());
        return hourlyRates;
    }

    @Transactional(readOnly = true)
    public HourlyRateDTO findById(Long id) {
        logger.info("Finding Hourly Rate by ID {}", id);
        HourlyRate hourlyRate = hourlyRateRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("HourlyRate", id));
        HourlyRateDTO hourlyRateDTO = hourlyRateMapper.toDTO(hourlyRate);
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
        return createdHourlyRateDTO;
    }

    @Transactional
    public HourlyRateDTO update(HourlyRateDTO hourlyRateDTO) {
        logger.info("Updating Hourly Rate {}", hourlyRateDTO);
        HourlyRate rate = hourlyRateRepository.findById(hourlyRateDTO.id())
                .orElseThrow(() -> new ObjectNotFoundException("HourlyRate", hourlyRateDTO.id()));
        hourlyRateMapper.updateEntityFromDto(hourlyRateDTO, rate);
        rate.setUpdatedAt(LocalDateTime.now());
        HourlyRateDTO updatedHourlyRateDTO = hourlyRateMapper.toDTO(hourlyRateRepository.save(rate));
        return updatedHourlyRateDTO;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting Hourly Rate {}", id);
        HourlyRate hourlyRate = hourlyRateRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("HourlyRate", id));

        hourlyRateRepository.delete(hourlyRate);
    }

}