package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.HourlyRateMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.repository.HourlyRateRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HourlyRateService {

    private final HourlyRateRepository hourlyRateRepository;
    private final HourlyRateMapper hourlyRateMapper;

    public HourlyRateService(HourlyRateRepository hourlyRateRepository, HourlyRateMapper hourlyRateMapper) {
        this.hourlyRateRepository = hourlyRateRepository;
        this.hourlyRateMapper = hourlyRateMapper;
    }

    public List<HourlyRateDTO> findAll() {
        return hourlyRateRepository.findAll().stream().map(hourlyRateMapper::toDTO).toList();
    }

    public HourlyRateDTO findById(Long id) {
        HourlyRate hourlyRate = hourlyRateRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Hourly Rate not found: ", id));
        return hourlyRateMapper.toDTO(hourlyRate);
    }

    public HourlyRateDTO create(HourlyRate hourlyRate) {
        return hourlyRateMapper.toDTO(hourlyRateRepository.save(hourlyRate));
    }

    public HourlyRateDTO update(Long id, HourlyRate hourlyRate) {
        HourlyRate rate = hourlyRateRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Hourly Rate not found: ", id));
        rate.setVehicleType(hourlyRate.getVehicleType());
        rate.setRatePerHour(hourlyRate.getRatePerHour());
        return hourlyRateMapper.toDTO(hourlyRateRepository.save(rate));
    }

    public void delete(Long hourlyRateId) {
        HourlyRate hourlyRate = hourlyRateRepository.findById(hourlyRateId).orElseThrow(() -> new RuntimeException("No records found for this ID"));
        hourlyRateRepository.delete(hourlyRate);
    }

}
