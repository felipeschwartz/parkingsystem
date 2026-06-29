package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.ParkingLotMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingLotDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingLot;
import com.github.felipeschwartz.parkingsystem.repository.ParkingLotRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingLotService {

    private Logger logger = LoggerFactory.getLogger(ParkingLotService.class.getName());

    private final ParkingLotRepository repository;
    private final ParkingLotMapper mapper;

    public ParkingLotService(ParkingLotRepository repository, ParkingLotMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ParkingLotDTO> findAll() {
        logger.info("Finding all Parking Lot!");
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ParkingLotDTO findById(Long id) {
        logger.info("Finding one Parking Lot!");
        ParkingLot parkingLot = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("ParkingLot not found: ", id));
        return mapper.toDTO(parkingLot);

    }

    // -------- CREATE --------


    @Transactional
    public ParkingLotDTO create(ParkingLotDTO dto) {
        logger.info("Creating one ParkingLot!");
        ParkingLot entity = mapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    // -------- UPDATE --------

    @Transactional
    public ParkingLotDTO update(Long id, ParkingLotDTO updated) {
        logger.info("Updating one ParkingLot!");
        ParkingLot entity = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("ParkingLot not found: ", id));
        mapper.updateEntityFromDto(updated, entity);
        entity.setUpdatedAt(LocalDateTime.now());
        entity = repository.save(entity);
        return mapper.toDTO(entity);

    }

    // -------- DELETE --------

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting one ParkingLot!");
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("ParkingLot not found: ", id);
        }
        repository.deleteById(id);
    }
}