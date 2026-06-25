package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.AddressMapper;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.felipeschwartz.parkingsystem.model.dto.OwnerDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.OwnerEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.OwnerIndividualDTO;
import com.github.felipeschwartz.parkingsystem.mapper.OwnerEntityMapper;
import com.github.felipeschwartz.parkingsystem.mapper.OwnerIndividualMapper;
import com.github.felipeschwartz.parkingsystem.model.entity.Owner;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerIndividual;
import com.github.felipeschwartz.parkingsystem.repository.OwnerEntityRepository;
import com.github.felipeschwartz.parkingsystem.repository.OwnerIndividualRepository;
import com.github.felipeschwartz.parkingsystem.repository.OwnerRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OwnerService {

    private Logger logger = LoggerFactory.getLogger(OwnerService.class.getName());
    private final OwnerRepository ownerRepository;
    private final OwnerIndividualRepository ownerIndividualRepository;
    private final OwnerEntityRepository ownerEntityRepository;
    private final OwnerEntityMapper entityMapper;
    private final OwnerIndividualMapper individualMapper;
    private final AddressMapper addressMapper;

    public OwnerService(OwnerRepository ownerRepository, OwnerIndividualRepository ownerIndividualRepository, OwnerEntityRepository ownerEntityRepository, OwnerEntityMapper entityMapper, OwnerIndividualMapper individualMapper, AddressMapper addressMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerIndividualRepository = ownerIndividualRepository;
        this.ownerEntityRepository = ownerEntityRepository;
        this.entityMapper = entityMapper;
        this.individualMapper = individualMapper;
        this.addressMapper = addressMapper;
    }

    @Transactional(readOnly = true)
    public List<OwnerDTO> findAll() {
        logger.info("Finding all Owners!");
        return ownerRepository.findAll()
                .stream()
                .map(owner -> {
                    if (owner instanceof OwnerIndividual oi) {
                        return (OwnerDTO) individualMapper.toDTO(oi);
                    } else if (owner instanceof OwnerEntity oe) {
                        return (OwnerDTO) entityMapper.toDTO(oe);
                    }
                    throw new IllegalStateException("Unknown owner type: " + owner.getClass());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<OwnerDTO> findOwnerById(Long ownerId) {
        logger.info("Finding one Owner!");
        return ownerRepository.findById(ownerId)
                .map(owner -> {
                    if (owner instanceof OwnerIndividual oi) {
                        return individualMapper.toDTO(oi);
                    } else if (owner instanceof OwnerEntity oe) {
                        return entityMapper.toDTO(oe);
                    }
                    throw new IllegalStateException("Unknown owner type: " + owner.getClass());
                });
    }

    @Transactional(readOnly = true)
    public Optional<OwnerIndividualDTO> findOwnerByCpf(String cpf) {
        logger.info("Finding one Owner by CPF!");
        return ownerIndividualRepository.findByCpf(cpf)
                .map(individualMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<OwnerEntityDTO> findOwnerByCnpj(String cnpj) {
        logger.info("Finding one Owner by CNPJ!");
        return ownerEntityRepository.findByCnpj(cnpj)
                .map(entityMapper::toDTO);
    }

    // -------- CREATE --------

    @Transactional
    public OwnerIndividualDTO createIndividual(OwnerIndividual individual) {
        logger.info("Creating one Individual Owner!");
        Objects.requireNonNull(individual, "individual must not be null.");

        String cpf = individual.getCpf();
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF must not be blank.");
        }
        if (ownerIndividualRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("An OwnerIndividual with this CPF already exists.");
        }


        if (individual.getVehicles() != null && !individual.getVehicles().isEmpty()) {
            for (Vehicle vehicle : individual.getVehicles()) {
                vehicle.setOwner(individual);
            }
        }
        return individualMapper.toDTO(ownerIndividualRepository.save(individual));
    }

    @Transactional
    public OwnerEntityDTO createEntity(OwnerEntity entity) {
        logger.info("Creating one Entity Owner!");
        Objects.requireNonNull(entity, "entity must not be null.");

        String cnpj = entity.getCnpj();
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ must not be blank.");
        }
        if (ownerEntityRepository.existsByCnpj(cnpj)) {
            throw new IllegalArgumentException("An OwnerEntity with this CNPJ already exists.");
        }


        if (entity.getVehicles() != null && !entity.getVehicles().isEmpty()) {
            for (Vehicle vehicle : entity.getVehicles()) {
                vehicle.setOwner(entity);
            }
        }
        return entityMapper.toDTO(ownerEntityRepository.save(entity));
    }


    // -------- UPDATE --------

    @Transactional
    public Optional<OwnerIndividualDTO> updateIndividual(Long id, OwnerIndividualDTO updated) {
        logger.info("Updating one Individual Owner!");
        return ownerIndividualRepository.findById(id)
                .map(individual -> {
                    if (updated.getPhone() != null) individual.setPhone(updated.getPhone());
                    if (updated.getEmail() != null) individual.setEmail(updated.getEmail());
                    if (updated.getFirstName() != null) individual.setFirstName(updated.getFirstName());
                    if (updated.getLastName() != null) individual.setLastName(updated.getLastName());
                    if (updated.getBirthDate() != null) individual.setBirthDate(updated.getBirthDate());
                    if (updated.getAddress() != null) individual.setAddress(addressMapper.toEntity(updated.getAddress()));
                    OwnerIndividual updatedOwner = ownerIndividualRepository.save(individual);
                    return individualMapper.toDTO(updatedOwner);
                });
    }

    @Transactional
    public Optional<OwnerEntityDTO> updateEntity(Long id, OwnerEntityDTO updated) {
        logger.info("Updating one Entity Owner!");
        return ownerEntityRepository.findById(id)
                .map(entity -> {
                    if (updated.getPhone() != null) entity.setPhone(updated.getPhone());
                    if (updated.getEmail() != null) entity.setEmail(updated.getEmail());
                    if (updated.getBillingContact() != null) entity.setBillingContact(updated.getBillingContact());
                    if (updated.getCorporateName() != null) entity.setCorporateName(updated.getCorporateName());
                    if (updated.getFantasyName() != null) entity.setFantasyName(updated.getFantasyName());
                    if (updated.getAddress() != null) entity.setAddress(addressMapper.toEntity(updated.getAddress()));
                    OwnerEntity updatedOwner = ownerEntityRepository.save(entity);
                    return entityMapper.toDTO(updatedOwner);
                });
    }


    // -------- DELETE --------

    @Transactional
    public void delete(Long ownerId) {
        logger.info("Deleting one Owner!");
        if (!ownerRepository.existsById(ownerId)) {
            throw new ObjectNotFoundException("Owner", ownerId);
        }
        ownerRepository.deleteById(ownerId);
    }
}