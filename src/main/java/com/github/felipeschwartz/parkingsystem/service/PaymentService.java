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
        return paymentRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public PaymentDTO findById(Long id) {
        logger.info("Finding Parking Lot!");
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Payment not found: ", id));
        return mapper.toDTO(payment);
    }

    // -------- CREATE --------

    @Transactional
    public PaymentDTO createPayment(Payment payment) {
        logger.info("Creating Payment!");
        return mapper.toDTO(paymentRepository.save(payment));
    }

    // -------- UPDATE --------

    @Transactional
    public PaymentDTO update(Long id, Payment updated) {
        logger.info("Updating Payment with id: {}", id);
        Payment entity = paymentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Payment not found: ", id));
        if (updated.getParkingSession() != null) entity.setParkingSession(updated.getParkingSession());
        if (updated.getAmount() != null) entity.setAmount(updated.getAmount());
        if (updated.getPaymentDate() != null) entity.setPaymentDate(updated.getPaymentDate());
        if (updated.getPaymentMethod() != null) entity.setPaymentMethod(updated.getPaymentMethod());
        if (updated.getPaymentStatus() != null) entity.setPaymentStatus(updated.getPaymentStatus());
        if (updated.getReference() != null) entity.setReference(updated.getReference());
        entity.setUpdatedDate(LocalDateTime.now());

        if (entity.getParkingSession() != null && entity.getPaymentStatus() == PAID) {
            ParkingSession parkingSession = entity.getParkingSession();
            parkingSession.setStatus(SessionStatus.CLOSED);
            parkingSession.setExitTime(LocalDateTime.now());
            parkingSessionRepository.save(parkingSession);
            logger.info("ParkingSession {} status updated to CLOSED due to payment.", parkingSession.getId());
        }

        return mapper.toDTO(paymentRepository.save(entity));
    }
}
