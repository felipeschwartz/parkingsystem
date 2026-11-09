package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.PaymentMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.PaymentDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import com.github.felipeschwartz.parkingsystem.model.entity.Payment;
import com.github.felipeschwartz.parkingsystem.model.enums.PaymentStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.repository.ParkingSessionRepository;
import com.github.felipeschwartz.parkingsystem.repository.PaymentRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private final PaymentRepository paymentRepository = mock(PaymentRepository.class);
    private final PaymentMapper mapper = mock(PaymentMapper.class);
    private final ParkingSessionRepository sessionRepository = mock(ParkingSessionRepository.class);
    private final PaymentService service = new PaymentService(paymentRepository, mapper, sessionRepository);

    private PaymentDTO dto(Long id) {
        return TestDtos.payment(id);
    }

    @Test
    void findAllShouldMapEveryPaymentAndAddLinks() {
        Payment payment = new Payment();
        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(mapper.toDTO(payment)).thenReturn(dto(1L));

        List<PaymentDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldReturnDtoWithLinks() {
        Payment payment = new Payment();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(mapper.toDTO(payment)).thenReturn(dto(1L));

        assertEquals(1L, service.findById(1L).id());
    }

    @Test
    void findByIdShouldFailWhenMissing() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));

        assertEquals("Payment not found by ID: 1", e.getMessage());
    }

    @Test
    void createShouldStampTimestampsAndSave() {
        Payment entity = new Payment();
        PaymentDTO input = dto(null);
        when(mapper.toEntity(input)).thenReturn(entity);
        when(paymentRepository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(2L));

        PaymentDTO result = service.create(input);

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void updateShouldFailWhenMissing() {
        when(paymentRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update(dto(3L)));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void updateShouldCloseTheSessionWhenPaymentBecomesPaid() {
        ParkingSession session = new ParkingSession();
        session.setStatus(SessionStatus.OPEN);
        Payment entity = new Payment();
        entity.setParkingSession(session);
        entity.setPaymentStatus(PaymentStatus.PAID);
        PaymentDTO input = dto(3L);
        when(paymentRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(paymentRepository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L));

        service.update(input);

        assertEquals(SessionStatus.CLOSED, session.getStatus());
        assertNotNull(session.getExitTime());
        verify(sessionRepository).save(session);
        verify(mapper).updateEntityFromDTO(input, entity);
    }

    @Test
    void updateShouldLeaveTheSessionAloneWhenPaymentIsNotPaid() {
        ParkingSession session = new ParkingSession();
        session.setStatus(SessionStatus.OPEN);
        Payment entity = new Payment();
        entity.setParkingSession(session);
        entity.setPaymentStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(paymentRepository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L));

        service.update(dto(3L));

        assertEquals(SessionStatus.OPEN, session.getStatus());
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void updateShouldSkipSessionHandlingWhenPaymentHasNoSession() {
        Payment entity = new Payment();
        entity.setPaymentStatus(PaymentStatus.PAID);
        when(paymentRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(paymentRepository.save(entity)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto(3L));

        service.update(dto(3L));

        verify(sessionRepository, never()).save(any());
    }
}
