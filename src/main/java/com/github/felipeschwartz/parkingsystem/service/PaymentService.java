package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.PaymentMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.PaymentDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import com.github.felipeschwartz.parkingsystem.model.entity.Payment;
import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.repository.ParkingSessionRepository;
import com.github.felipeschwartz.parkingsystem.repository.PaymentRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.felipeschwartz.parkingsystem.model.enums.PaymentStatus.PAID;

@Service
public class PaymentService {
    private Logger logger = LoggerFactory.getLogger(PaymentService.class.getName());

    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final ParkingSessionRepository parkingSessionRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper mapper, ParkingSessionRepository parkingSessionRepository) {
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
        this.parkingSessionRepository = parkingSessionRepository;
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAll() {
        logger.info("Finding all Payments!");
        List<PaymentDTO> paymentDTOS = paymentRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return paymentDTOS;
    }

    @Transactional(readOnly = true)
    public PaymentDTO findById(Long id) {
        logger.info("Finding Parking Lot!");
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Payment", id));
        PaymentDTO paymentDTO = mapper.toDTO(payment);
        return paymentDTO;
    }

    @Transactional
    public PaymentDTO create(PaymentDTO paymentDTO) {
        logger.info("Creating Payment!");
        Payment payment = mapper.toEntity(paymentDTO);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        PaymentDTO paymentDTOCreated = mapper.toDTO(paymentRepository.save(payment));
        return paymentDTOCreated;
    }

    @Transactional
    public PaymentDTO update(PaymentDTO updated) {
        logger.info("Updating Payment with id: {}", updated.id());
        Payment entity = paymentRepository.findById(updated.id())
                .orElseThrow(() -> new ObjectNotFoundException("Payment", updated.id()));
        mapper.updateEntityFromDTO(updated, entity);
        entity.setUpdatedAt(LocalDateTime.now());

        if (entity.getParkingSession() != null && entity.getPaymentStatus() == PAID) {
            ParkingSession parkingSession = entity.getParkingSession();
            parkingSession.setStatus(SessionStatus.CLOSED);
            parkingSession.setExitTime(LocalDateTime.now());
            parkingSessionRepository.save(parkingSession);
            logger.info("ParkingSession {} status updated to CLOSED due to payment.", parkingSession.getId());
        }
        PaymentDTO paymentDTOUpdated = mapper.toDTO(paymentRepository.save(entity));
        return paymentDTOUpdated;
    }

}
