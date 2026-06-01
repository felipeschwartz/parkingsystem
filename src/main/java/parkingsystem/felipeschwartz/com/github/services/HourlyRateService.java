package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.HourlyRate;
import parkingsystem.felipeschwartz.com.github.model.entities.PlanRate;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import parkingsystem.felipeschwartz.com.github.repositories.HourlyRateRepository;
import parkingsystem.felipeschwartz.com.github.repositories.PlanRateRepository;

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

    public HourlyRate create(HourlyRate hourlyRate) {
        return hourlyRateRepository.save(hourlyRate);
    }

    public HourlyRate findById(Long hourlyRateId) {
        return hourlyRateRepository.findById(hourlyRateId).orElseThrow(() -> new RuntimeException("No records found for this ID"));
    }

    public HourlyRate update(HourlyRate hourlyRate) {
        HourlyRate rate = hourlyRateRepository.findById(hourlyRate.getId()).orElseThrow(() -> new RuntimeException("No records found for this ID"));
        rate.setVehicleType(hourlyRate.getVehicleType());
        rate.setPricePerHour(hourlyRate.getPricePerHour());
        return hourlyRateRepository.save(rate);
    }

    public void delete(Long hourlyRateId) {
        HourlyRate hourlyRate = hourlyRateRepository.findById(hourlyRateId).orElseThrow(() -> new RuntimeException("No records found for this ID"));
        hourlyRateRepository.delete(hourlyRate);
    }

}
