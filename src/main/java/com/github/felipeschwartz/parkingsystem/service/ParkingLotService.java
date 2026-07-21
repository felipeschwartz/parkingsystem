package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.controller.ParkingLotController;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<ParkingLotDTO> parkingLots = repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        parkingLots.forEach(this::addHateoasLinks);
        return parkingLots;
    }

    @Transactional(readOnly = true)
    public ParkingLotDTO findById(Long id) {
        logger.info("Finding Parking Lot with id {}", id);
        ParkingLot parkingLot = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("ParkingLot not found: ", id));
        ParkingLotDTO parkingLotDTO = mapper.toDTO(parkingLot);
        addHateoasLinks(parkingLotDTO);
        return parkingLotDTO;
    }


    @Transactional
    public ParkingLotDTO create(ParkingLotDTO parkingLotDTO) {
        logger.info("Creating one ParkingLot!");
        ParkingLot parkingLot = mapper.toEntity(parkingLotDTO);
        parkingLot.setCreatedAt(LocalDateTime.now());
        parkingLot.setUpdatedAt(LocalDateTime.now());
        ParkingLotDTO createdParkingLotDTO = mapper.toDTO(repository.save(parkingLot));
        addHateoasLinks(createdParkingLotDTO);
        return createdParkingLotDTO;
    }


    @Transactional
    public ParkingLotDTO update(ParkingLotDTO parkingLotDTO) {
        logger.info("Updating one ParkingLot!");
        ParkingLot parkingLot = repository.findById(parkingLotDTO.getId())
                .orElseThrow(() -> new ObjectNotFoundException("ParkingLot not found: ", parkingLotDTO.getId()));
        mapper.updateEntityFromDto(parkingLotDTO, parkingLot);
        parkingLot.setUpdatedAt(LocalDateTime.now());
        ParkingLotDTO updatedParkingLotDTO = mapper.toDTO(repository.save(parkingLot));
        addHateoasLinks(updatedParkingLotDTO);
        return updatedParkingLotDTO;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting Hourly Rate {}", id);
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("ParkingLot not found: ", id);
        }
        repository.deleteById(id);
    }


    private void addHateoasLinks(ParkingLotDTO dto) {
        dto.add(linkTo(methodOn(ParkingLotController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(ParkingLotController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(ParkingLotController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(ParkingLotController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(ParkingLotController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}