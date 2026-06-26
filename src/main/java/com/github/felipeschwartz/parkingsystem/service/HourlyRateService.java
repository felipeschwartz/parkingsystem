package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.HourlyRateMapper; 
import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO; 
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.repository.HourlyRateRepository;
import org.springframework.transaction.annotation.Transactional; 

import java.util.List;
import java.util.stream.Collectors; 

@Service
public class HourlyRateService {

    private final HourlyRateRepository hourlyRateRepository;
    private final HourlyRateMapper hourlyRateMapper; 

    public HourlyRateService(HourlyRateRepository hourlyRateRepository, HourlyRateMapper hourlyRateMapper) { 
        this.hourlyRateRepository = hourlyRateRepository;
        this.hourlyRateMapper = hourlyRateMapper;
    }

    @Transactional(readOnly = true) // Adicionado
    public List<HourlyRateDTO> findAll() {
        return hourlyRateRepository.findAll().stream()
                .map(hourlyRateMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HourlyRateDTO findById(Long hourlyRateId) {
        HourlyRate hourlyRate = hourlyRateRepository.findById(hourlyRateId)
                .orElseThrow(() -> new ObjectNotFoundException("HourlyRate", hourlyRateId));
        return hourlyRateMapper.toDTO(hourlyRate);
    }

    @Transactional
    public HourlyRateDTO create(HourlyRateDTO hourlyRateDTO) {
        HourlyRate hourlyRate = hourlyRateMapper.toEntity(hourlyRateDTO);
        hourlyRate = hourlyRateRepository.save(hourlyRate);
        return hourlyRateMapper.toDTO(hourlyRate);
    }

    @Transactional
    public HourlyRateDTO update(Long id, HourlyRateDTO hourlyRateDTO) {
        HourlyRate rate = hourlyRateRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("HourlyRate", id));

        if (hourlyRateDTO.getVehicleType() != null) rate.setVehicleType(hourlyRateDTO.getVehicleType());
        if (hourlyRateDTO.getRatePerHour() != null) rate.setRatePerHour(hourlyRateDTO.getRatePerHour());
        if (hourlyRateDTO.getActive() != null) rate.setActive(hourlyRateDTO.getActive());

        rate = hourlyRateRepository.save(rate);
        return hourlyRateMapper.toDTO(rate);
    }

    @Transactional
    public void delete(Long hourlyRateId) {
        if (!hourlyRateRepository.existsById(hourlyRateId)) {
            throw new ObjectNotFoundException("HourlyRate", hourlyRateId);
        }
        hourlyRateRepository.deleteById(hourlyRateId);
    }
}