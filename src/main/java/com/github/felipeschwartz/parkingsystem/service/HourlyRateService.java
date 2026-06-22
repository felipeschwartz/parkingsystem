package com.github.felipeschwartz.parkingsystem.service;

import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.repository.HourlyRateRepository;

import java.util.List;

@Service
public class HourlyRateService {

    private final HourlyRateRepository hourlyRateRepository;

    public HourlyRateService(HourlyRateRepository hourlyRateRepository) {
        this.hourlyRateRepository = hourlyRateRepository;
    }

    public List<HourlyRate> findAll() {
        return hourlyRateRepository.findAll();
    }

    public HourlyRate findById(Long hourlyRateId) {
        return hourlyRateRepository.findById(hourlyRateId).orElseThrow(() -> new RuntimeException("No records found for this ID"));
    }

    public HourlyRate create(HourlyRate hourlyRate) {
        return hourlyRateRepository.save(hourlyRate);
    }

    public HourlyRate update(HourlyRate hourlyRate) {
        HourlyRate rate = hourlyRateRepository.findById(hourlyRate.getId()).orElseThrow(() -> new RuntimeException("No records found for this ID"));
        rate.setVehicleType(hourlyRate.getVehicleType());
        rate.setRatePerHour(hourlyRate.getRatePerHour());
        return hourlyRateRepository.save(rate);
    }

    public void delete(Long hourlyRateId) {
        HourlyRate hourlyRate = hourlyRateRepository.findById(hourlyRateId).orElseThrow(() -> new RuntimeException("No records found for this ID"));
        hourlyRateRepository.delete(hourlyRate);
    }

}
