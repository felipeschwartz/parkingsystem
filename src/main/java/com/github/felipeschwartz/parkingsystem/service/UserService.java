package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.AddressMapper;
import com.github.felipeschwartz.parkingsystem.mapper.UserEntityMapper;
import com.github.felipeschwartz.parkingsystem.mapper.UserIndividualMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.*;
import com.github.felipeschwartz.parkingsystem.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.felipeschwartz.parkingsystem.repository.UserEntityRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserIndividualRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class.getName());
    private final UserRepository userRepository;
    private final UserIndividualRepository userIndividualRepository;
    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper entityMapper;
    private final UserIndividualMapper individualMapper;
    private final AddressMapper addressMapper;

    public UserService(UserRepository userRepository, UserIndividualRepository userIndividualRepository, UserEntityRepository userEntityRepository, UserEntityMapper entityMapper, UserIndividualMapper individualMapper, AddressMapper addressMapper) {
        this.userRepository = userRepository;
        this.userIndividualRepository = userIndividualRepository;
        this.userEntityRepository = userEntityRepository;
        this.entityMapper = entityMapper;
        this.individualMapper = individualMapper;
        this.addressMapper = addressMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        logger.info("Finding all Users!");
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    if (user instanceof UserIndividual oi) {
                        return (UserDTO) individualMapper.toDTO(oi);
                    } else if (user instanceof UserEntity oe) {
                        return (UserDTO) entityMapper.toDTO(oe);
                    }
                    throw new IllegalStateException("Unknown user type: " + user.getClass());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> findUserById(Long userId) {
        logger.info("Finding one User!");
        return userRepository.findById(userId)
                .map(user -> {
                    if (user instanceof UserIndividual oi) {
                        return individualMapper.toDTO(oi);
                    } else if (user instanceof UserEntity oe) {
                        return entityMapper.toDTO(oe);
                    }
                    throw new IllegalStateException("Unknown user type: " + user.getClass());
                });
    }

    @Transactional(readOnly = true)
    public Optional<UserIndividualDTO> findUserByCpf(String cpf) {
        logger.info("Finding one User by CPF!");
        return userIndividualRepository.findByCpf(cpf)
                .map(individualMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntityDTO> findUserByCnpj(String cnpj) {
        logger.info("Finding one User by CNPJ!");
        return userEntityRepository.findByCnpj(cnpj)
                .map(entityMapper::toDTO);
    }

    // -------- CREATE --------

    @Transactional
    public UserIndividualDTO createIndividual(UserIndividual individual) {
        logger.info("Creating one Individual User!");
        Objects.requireNonNull(individual, "individual must not be null.");

        String cpf = individual.getCpf();
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF must not be blank.");
        }
        if (userIndividualRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("An UserIndividual with this CPF already exists.");
        }


        if (individual.getVehicles() != null && !individual.getVehicles().isEmpty()) {
            for (Vehicle vehicle : individual.getVehicles()) {
                vehicle.setUser(individual);
            }
        }
        return individualMapper.toDTO(userIndividualRepository.save(individual));
    }

    @Transactional
    public UserEntityDTO createEntity(UserEntity entity) {
        logger.info("Creating one Entity User!");
        Objects.requireNonNull(entity, "entity must not be null.");

        String cnpj = entity.getCnpj();
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ must not be blank.");
        }
        if (userEntityRepository.existsByCnpj(cnpj)) {
            throw new IllegalArgumentException("An UserEntity with this CNPJ already exists.");
        }


        if (entity.getVehicles() != null && !entity.getVehicles().isEmpty()) {
            for (Vehicle vehicle : entity.getVehicles()) {
                vehicle.setUser(entity);
            }
        }
        return entityMapper.toDTO(userEntityRepository.save(entity));
    }


    // -------- UPDATE --------

    @Transactional
    public Optional<UserIndividualDTO> updateIndividual(Long id, UserIndividualDTO updated) {
        logger.info("Updating one Individual User!");
        return userIndividualRepository.findById(id)
                .map(individual -> {
                    if (updated.getPhone() != null) individual.setPhone(updated.getPhone());
                    if (updated.getEmail() != null) individual.setEmail(updated.getEmail());
                    if (updated.getFirstName() != null) individual.setFirstName(updated.getFirstName());
                    if (updated.getLastName() != null) individual.setLastName(updated.getLastName());
                    if (updated.getBirthDate() != null) individual.setBirthDate(updated.getBirthDate());
                    if (updated.getAddress() != null) individual.setAddress(addressMapper.toEntity(updated.getAddress()));
                    UserIndividual updatedUser = userIndividualRepository.save(individual);
                    return individualMapper.toDTO(updatedUser);
                });
    }

    @Transactional
    public Optional<UserEntityDTO> updateEntity(Long id, UserEntityDTO updated) {
        logger.info("Updating one Entity User!");
        return userEntityRepository.findById(id)
                .map(entity -> {
                    if (updated.getPhone() != null) entity.setPhone(updated.getPhone());
                    if (updated.getEmail() != null) entity.setEmail(updated.getEmail());
                    if (updated.getBillingContact() != null) entity.setBillingContact(updated.getBillingContact());
                    if (updated.getCorporateName() != null) entity.setCorporateName(updated.getCorporateName());
                    if (updated.getFantasyName() != null) entity.setFantasyName(updated.getFantasyName());
                    if (updated.getAddress() != null) entity.setAddress(addressMapper.toEntity(updated.getAddress()));
                    UserEntity updatedUser = userEntityRepository.save(entity);
                    return entityMapper.toDTO(updatedUser);
                });
    }


    // -------- DELETE --------

    @Transactional
    public void delete(Long userId) {
        logger.info("Deleting one User!");
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User", userId);
        }
        userRepository.deleteById(userId);
    }
}