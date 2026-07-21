package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.controller.SubscriptionContractController;
import com.github.felipeschwartz.parkingsystem.mapper.CycleAvoidingMappingContext;
import com.github.felipeschwartz.parkingsystem.mapper.SubscriptionContractMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import com.github.felipeschwartz.parkingsystem.model.entity.User;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.repository.PlanRepository;
import com.github.felipeschwartz.parkingsystem.repository.SubscriptionContractRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import com.github.felipeschwartz.parkingsystem.repository.VehicleRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class SubscriptionContractService {

    private Logger logger = LoggerFactory.getLogger(PaymentService.class.getName());
    private final SubscriptionContractRepository subscriptionContractRepository;
    private final SubscriptionContractMapper subscriptionContractMapper;
    private final CycleAvoidingMappingContext context;
    private final PlanRepository planRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public SubscriptionContractService(SubscriptionContractRepository subscriptionContractRepository, SubscriptionContractMapper subscriptionContractMapper, CycleAvoidingMappingContext context, PlanRepository planRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.subscriptionContractRepository = subscriptionContractRepository;
        this.subscriptionContractMapper = subscriptionContractMapper;
        this.context = context;
        this.planRepository = planRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<SubscriptionContractDTO> findAll() {
        logger.info("Finding all Subscription contracts records");
        CycleAvoidingMappingContext context = new CycleAvoidingMappingContext();
        List<SubscriptionContractDTO> contracts = subscriptionContractRepository.findAllWithUsers()
                .stream().map(entity -> subscriptionContractMapper.toDTO(entity, context))
                .collect(Collectors.toList());
        contracts.forEach(this::addHateoasLinks);
        return contracts;
    }

    @Transactional(readOnly = true)
    public SubscriptionContractDTO findById(Long id) {
        logger.info("Finding Subscription contract record with id {}", id);
        SubscriptionContract subscriptionContract = subscriptionContractRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Subscription contract with id " + id + " not found!"));
        SubscriptionContractDTO subscriptionContractDTO = subscriptionContractMapper.toDTO(subscriptionContract, context);
        addHateoasLinks(subscriptionContractDTO);
        return subscriptionContractDTO;
    }

    @Transactional
    public SubscriptionContractDTO create(SubscriptionContractDTO dto) {
        logger.info("Creating Subscription contract record {}", dto);
        validateDates(dto.getStartDate(), dto.getEndDate());
        Plan plan = planRepository.findById(dto.getpId())
                .orElseThrow(() -> new ObjectNotFoundException("Plan with id " + dto.getpId() + " not found!"));
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicle().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle with id " + dto.getVehicle().getId() + " not found!"));
        User user = userRepository.findById(dto.getUser().getId())
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + dto.getUser().getId() + " not found!"));
        SubscriptionContract subscriptionToSave = subscriptionContractMapper.toEntity(dto);
        subscriptionToSave.setPlan(plan);
        subscriptionToSave.setVehicle(vehicle);
        subscriptionToSave.setUser(user);

        SubscriptionContract savedSubscription =  subscriptionContractRepository.save(subscriptionToSave);
        Set<SubscriptionContract> currentContracts = plan.getSubscriptionContracts();
        if (currentContracts == null) {
            currentContracts = new HashSet<>();
        }
        currentContracts.add(savedSubscription);
        plan.setSubscriptionContracts(currentContracts);
        planRepository.save(plan);
        SubscriptionContractDTO subscriptionContractDTO = subscriptionContractMapper.toDTO(savedSubscription, context);
        addHateoasLinks(subscriptionContractDTO);

        return subscriptionContractDTO;
    }

    @Transactional
    public SubscriptionContractDTO update(SubscriptionContractDTO dto) {
        logger.info("Updating Subscription contract with id {}", dto.getId());
        SubscriptionContract existingSubscription = subscriptionContractRepository.findById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Subscription contract with id " + dto.getId() + " not found!"));
        subscriptionContractMapper.updateSubscriptionContractFromDto(dto, existingSubscription);
        existingSubscription.setUpdatedAt(LocalDateTime.now());
        SubscriptionContractDTO updatedSubscriptionDTO = subscriptionContractMapper.toDTO(existingSubscription, context);
        addHateoasLinks(updatedSubscriptionDTO);
        return updatedSubscriptionDTO;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting Subscription contract record {}", id);
        if (!subscriptionContractRepository.existsById(id)) {
            throw new ObjectNotFoundException("Subscription contract with id " + id + " not found!");
        }
        subscriptionContractRepository.deleteById(id);
    }

    @Transactional
    public SubscriptionContractDTO renew(SubscriptionContractDTO dto) {
        logger.info("Renewing Subscription contract record {}", dto);
        SubscriptionContract existingSubscription = subscriptionContractRepository.findById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Subscription contract with id " + dto.getId() + " not found!"));
        if (dto.getEndDate() == null) {
            throw new IllegalArgumentException("New end date must be provided in the DTO for renewal.");
        }

        existingSubscription.renew(dto.getEndDate());
        existingSubscription.setUpdatedAt(LocalDateTime.now());
        SubscriptionContractDTO updatedSubscriptionDTO = subscriptionContractMapper.toDTO(existingSubscription, context);
        addHateoasLinks(updatedSubscriptionDTO);
        return updatedSubscriptionDTO;
    }



//    @Transactional
//    public SubscriptionContract createContract(SubscriptionContract contract) {
//        validateDates(contract.getStartDate(), contract.getEndDate());
//
//        // Se endDate for null (sem fim), use uma data "bem grande" só para checar sobreposição
//        LocalDate end = (contract.getEndDate() == null) ? LocalDate.of(9999, 12, 31) : contract.getEndDate();
//
//        List<SubscriptionContract> overlaps = subscriptionContractRepository.findOverlappingContracts(
//                contract.getVehicle().getId(),
//                SubscripionStatus.ACTIVE,
//                contract.getStartDate(),
//                end
//        );
//
//        if (!overlaps.isEmpty()) {
//            throw new IllegalStateException("There is already an active contract with an overlapping period for this vehicle.");
//        }
//
//        // Garante status default
//        if (contract.getStatus() == null) {
//            contract.setStatus(SubscripionStatus.ACTIVE);
//        }
//
//        return subscriptionContractRepository.save(contract);
//    }



    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("startDate cannot be null or before today.");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be earlier than startDate.");
        }
    }

    private void addHateoasLinks(SubscriptionContractDTO dto) {
        dto.add(linkTo(methodOn(SubscriptionContractController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(SubscriptionContractController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(SubscriptionContractController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(SubscriptionContractController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(SubscriptionContractController.class).update(dto)).withRel("renew").withType("PUT"));
        dto.add(linkTo(methodOn(SubscriptionContractController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
