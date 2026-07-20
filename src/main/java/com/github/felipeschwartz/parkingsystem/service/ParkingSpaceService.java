package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.controller.ParkingSpaceController;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ParkingSpaceService {
    private Logger logger = LoggerFactory.getLogger(ParkingSpaceService.class.getName());

    private final ParkingSpaceRepository repository;
    private final ParkingSpaceMapper mapper;

    public ParkingSpaceService(ParkingSpaceRepository repository, ParkingSpaceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ParkingSpaceDTO> findAll() {
        logger.info("Finding all Parking Spaces!");
        List<ParkingSpaceDTO> parkingSpaces = repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        parkingSpaces.forEach(this::addHateoasLinks);
        return parkingSpaces;
    }

    @Transactional(readOnly = true)
    public ParkingSpaceDTO findById(Long id) {
        logger.info("Finding Parking Space!");
        ParkingSpace parkingSpace = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Parking Space not found: ", id));
        ParkingSpaceDTO parkingSpaceDTO = mapper.toDTO(parkingSpace);
        addHateoasLinks(parkingSpaceDTO);
        return parkingSpaceDTO;
    }

    // -------- CREATE --------

    @Transactional
    public ParkingSpaceDTO createParkingSpace(ParkingSpaceDTO parkingSpaceDTO) {
        logger.info("Creating Parking Space!");
        ParkingSpace parkingSpace = mapper.toEntity(parkingSpaceDTO);
        parkingSpace.setCreatedAt(LocalDateTime.now());
        parkingSpace.setUpdatedAt(LocalDateTime.now());
        ParkingSpaceDTO savedParkingSpaceDTO = mapper.toDTO(repository.save(parkingSpace));
        addHateoasLinks(savedParkingSpaceDTO);
        return savedParkingSpaceDTO;
    }

    // -------- UPDATE --------

    @Transactional
    public ParkingSpaceDTO updateParkingSpace(ParkingSpaceDTO parkingSpaceDTO) {
        logger.info("Updating Parking Space!");
        ParkingSpace parkingSpace = repository.findById(parkingSpaceDTO.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Parking Space not found: ", parkingSpaceDTO.getId()));
        mapper.updateEntityFromDto(parkingSpaceDTO, parkingSpace);
        parkingSpace.setUpdatedAt(LocalDateTime.now());
        ParkingSpaceDTO updatedParkingSpaceDTO = mapper.toDTO(repository.save(parkingSpace));
        addHateoasLinks(updatedParkingSpaceDTO);
        return updatedParkingSpaceDTO;
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

    private void addHateoasLinks(ParkingSpaceDTO dto) {
        dto.add(linkTo(methodOn(ParkingSpaceController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(ParkingSpaceController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(ParkingSpaceController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(ParkingSpaceController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(ParkingSpaceController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
