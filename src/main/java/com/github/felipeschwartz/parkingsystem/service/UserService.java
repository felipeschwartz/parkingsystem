package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.UserCreationMapper;
import com.github.felipeschwartz.parkingsystem.mapper.UserEntityMapper;
import com.github.felipeschwartz.parkingsystem.mapper.UserIndividualMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.User;
import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import com.github.felipeschwartz.parkingsystem.repository.UserEntityRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserIndividualRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final UserIndividualRepository userIndividualRepository;
    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper entityMapper;
    private final UserIndividualMapper individualMapper;
    private final UserCreationMapper userCreationMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserIndividualRepository userIndividualRepository,
                       UserEntityRepository userEntityRepository, UserEntityMapper entityMapper,
                       UserIndividualMapper individualMapper, UserCreationMapper userCreationMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userIndividualRepository = userIndividualRepository;
        this.userEntityRepository = userEntityRepository;
        this.entityMapper = entityMapper;
        this.individualMapper = individualMapper;
        this.userCreationMapper = userCreationMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PARKING_MANAGER')")
    public List<UserDTO> findAll() {
        logger.info("Finding all Users!");
        List<UserDTO> userDTOS = userRepository.findAll()
                .stream()
                .map(user -> {
                    if (user instanceof UserIndividual oi) {
                        return (UserDTO) individualMapper.toDTO(oi);
                    } else if (user instanceof UserEntity oe) {
                        return (UserDTO) entityMapper.toDTO(oe);
                    }
                    throw new IllegalStateException("Unknown user type: " + user.getId());
                }).collect(Collectors.toList());
        return userDTOS;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PARKING_MANAGER') or #id == authentication.principal.id")
    public UserDTO findById(Long id) {
        logger.info("Finding one User by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User", id));
        UserDTO userDTO;
        if (user instanceof UserIndividual oi) {
            userDTO = individualMapper.toDTO(oi);
        } else if (user instanceof UserEntity oe) {
            userDTO = entityMapper.toDTO(oe);
        } else {
            throw new IllegalStateException("Unknown user type: " + user.getClass().getName());
        }
        return userDTO;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PARKING_MANAGER') or #cpf == authentication.principal.cpf")
    public UserIndividualDTO findByCpf(String cpf) {
        logger.info("Finding one User by CPF: {}", cpf);
        UserIndividual userIndividual = userIndividualRepository.findByCpf(cpf)
                .orElseThrow(() -> new ObjectNotFoundException("User with CPF", cpf));
        UserIndividualDTO userDTO = individualMapper.toDTO(userIndividual);
        return userDTO;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PARKING_MANAGER') or #cnpj == authentication.principal.cnpj")
    public UserEntityDTO findByCnpj(String cnpj) {
        logger.info("Finding one User by CNPJ: {}", cnpj);
        UserEntity userEntity = userEntityRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new ObjectNotFoundException("User with CNPJ", cnpj));
        UserEntityDTO userDTO = entityMapper.toDTO(userEntity);
        return userDTO;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO create(CreateUserRequestDTO requestDTO) {
        logger.info("Creating a User of type: {}", requestDTO.userType());
        Objects.requireNonNull(requestDTO, "User creation request must not be null");
        User user;
        if (requestDTO.userType() == UserType.INDIVIDUAL) {
            if (requestDTO.cpf() == null || requestDTO.cpf().isBlank()) {
                throw new IllegalArgumentException("CPF must not be blank for individual users.");
            }
            if (userIndividualRepository.existsByCpf(requestDTO.cpf())) {
                throw new IllegalStateException("User with CPF already exists.");
            }
            user = userCreationMapper.toIndividualEntity(requestDTO);
        } else if (requestDTO.userType() == UserType.ENTITY) {
            if (requestDTO.cnpj() == null || requestDTO.cnpj().isBlank()) {
                throw new IllegalArgumentException("CNPJ must not be blank for entity users.");
            }
            if (userEntityRepository.existsByCnpj(requestDTO.cnpj())) {
                throw new IllegalStateException("User with CNPJ already exists.");
            }
            user = userCreationMapper.toEntityEntity(requestDTO);
        } else  {
            throw new IllegalArgumentException("Unsupported UserType for creation: " + requestDTO.userType());
        }
        user.setPassword(passwordEncoder.encode(requestDTO.password()));
        user.setRoles(requestDTO.userProfile().roles());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        if (requestDTO.vehicles() != null && !requestDTO.vehicles().isEmpty()) {
            Set<Vehicle> vehicles = requestDTO.vehicles().stream()
                    .map(vehicleDTO -> {
                        Vehicle vehicle = new Vehicle();
                        vehicle.setLicensePlate(vehicleDTO.licensePlate());
                        vehicle.setType(vehicleDTO.type());
                        vehicle.setUser(user);
                        return vehicle;
                    })
                    .collect(Collectors.toSet());
            user.setVehicles(vehicles);
        }
        User savedUser = userRepository.save(user);
        UserDTO createdUserDTO;
        if (savedUser instanceof UserIndividual oi) {
            createdUserDTO = individualMapper.toDTO(oi);
        } else if (savedUser instanceof UserEntity oe) {
            createdUserDTO = entityMapper.toDTO(oe);
        } else {
            throw new IllegalStateException("Saved user is of an unknown type.");
        }
        return createdUserDTO;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserDTO update(Long id, CreateUserRequestDTO updatedDTO) {
        logger.info("Updating User with ID: {}", id);
        Objects.requireNonNull(updatedDTO, "User update request must not be null.");

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User", id));

        if (existingUser.getUserType() != updatedDTO.userType()) {
            throw new IllegalArgumentException("Cannot change user type during update. Existing user is "
                    + existingUser.getUserType() + ", but provided " + updatedDTO.userType());
        }
        if (existingUser instanceof UserIndividual individual) {
            if (updatedDTO.userType() != UserType.INDIVIDUAL) {
                throw new IllegalArgumentException("Mismatched user type for update. Expected INDIVIDUAL, got "
                        + updatedDTO.userType());
            }
            if (updatedDTO.cpf() != null && !updatedDTO.cpf().isBlank()
                    && !updatedDTO.cpf().equals(individual.getCpf())) {
                if (userIndividualRepository.existsByCpf(updatedDTO.cpf())) {
                    throw new IllegalStateException("An UserIndividual with this CPF already exists.");
                }
            }
            individualMapper.updateIndividualFromCreateRequest(updatedDTO, individual);
            individual.setUpdatedAt(LocalDateTime.now());
            UserIndividual updatedIndividual = userIndividualRepository.save(individual);
            UserIndividualDTO resultDTO = individualMapper.toDTO(updatedIndividual);
            return resultDTO;

        }  else if (existingUser instanceof UserEntity entity) {
            if  (updatedDTO.userType() != UserType.ENTITY) {
                throw new IllegalArgumentException("Mismatched user type for update. Expected ENTITY, got "
                        + updatedDTO.userType());
            }
            if (updatedDTO.cnpj() != null && !updatedDTO.cnpj().isBlank()
                    && !updatedDTO.cnpj().equals(entity.getCnpj())) {
                if (userEntityRepository.existsByCnpj(updatedDTO.cnpj())) {
                    throw new IllegalStateException("An User with this CNPJ already exists.");
                }
            }
            entityMapper.updateEntityFromCreateRequest(updatedDTO, entity);
            entity.setUpdatedAt(LocalDateTime.now());
            UserEntity updatedEntity = userRepository.save(entity);
            UserEntityDTO resultDTO = entityMapper.toDTO(updatedEntity);
            return resultDTO;
        } else {
            throw new IllegalStateException("Existing user is of an unknown type.");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        logger.info("Deleting one User!");
        if (!userRepository.existsById(id)) {
            throw new ObjectNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

}