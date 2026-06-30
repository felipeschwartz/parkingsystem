package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.ParkingSpaceMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSpaceDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.repository.ParkingSpaceRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingSpaceService {
    private Logger logger = LoggerFactory.getLogger(ParkingLotService.class.getName());

    private final ParkingSpaceRepository repository;
    private final ParkingSpaceMapper mapper;

    public ParkingSpaceService(ParkingSpaceRepository repository, ParkingSpaceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ParkingSpaceDTO> findAllParkingSpaces() {
        logger.info("Finding all Parking Spaces!");
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ParkingSpaceDTO findParkingSpace(Long id) {
        logger.info("Finding Parking Lot!");
        ParkingSpace parkingSpace = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Parking Space not found: ", id));
        return mapper.toDTO(parkingSpace);
    }

    // -------- CREATE --------

    @Transactional
    public ParkingSpaceDTO createParkingSpace(ParkingSpaceDTO dto) {
        logger.info("Creating Parking Space!");
        ParkingSpace entity = mapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    // -------- UPDATE --------

    @Transactional
    public ParkingSpaceDTO updateParkingSpace(Long id, ParkingSpaceDTO dto) {
        logger.info("Updating Parking Space!");
        ParkingSpace entity = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Parking Space not found: ", id));
        mapper.updateEntityFromDto(dto, entity);
        entity.setUpdatedAt(LocalDateTime.now());
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    // -------- DELETE --------

    @Transactional
    public void deleteParkingSpace(Long id) {
        logger.info("Deleting Parking Space!");
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("Parking Space not found: ", id);
        }
        repository.deleteById(id);
    }
}
