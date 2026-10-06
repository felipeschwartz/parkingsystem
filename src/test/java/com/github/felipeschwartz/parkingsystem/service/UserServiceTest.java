package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.UserCreationMapper;
import com.github.felipeschwartz.parkingsystem.mapper.UserEntityMapper;
import com.github.felipeschwartz.parkingsystem.mapper.UserIndividualMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import com.github.felipeschwartz.parkingsystem.repository.UserEntityRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserIndividualRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserIndividualRepository individualRepository = mock(UserIndividualRepository.class);
    private final UserEntityRepository entityRepository = mock(UserEntityRepository.class);

    private final UserService service = new UserService(
            userRepository, individualRepository, entityRepository,
            mock(UserEntityMapper.class), mock(UserIndividualMapper.class), mock(UserCreationMapper.class),
            mock(PasswordEncoder.class));

    @Test
    void shouldRejectBlankCpfAsInvalidArgument() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.create(request(UserType.INDIVIDUAL, " ", null)));

        assertEquals("CPF must not be blank for individual users.", e.getMessage());
    }

    @Test
    void shouldRejectDuplicateCpfAsConflict() {
        when(individualRepository.existsByCpf("11111111111")).thenReturn(true);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> service.create(request(UserType.INDIVIDUAL, "11111111111", null)));

        assertEquals("User with CPF already exists.", e.getMessage());
    }

    @Test
    void shouldRejectBlankCnpjAsInvalidArgument() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.create(request(UserType.ENTITY, null, "")));

        assertEquals("CNPJ must not be blank for entity users.", e.getMessage());
    }

    @Test
    void shouldRejectDuplicateCnpjAsConflict() {
        when(entityRepository.existsByCnpj("11111111000111")).thenReturn(true);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> service.create(request(UserType.ENTITY, null, "11111111000111")));

        assertEquals("User with CNPJ already exists.", e.getMessage());
    }

    @Test
    void shouldReportMissingUserWithEntityNameAndId() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findById(5L));

        assertEquals("User not found by ID: 5", e.getMessage());
    }

    private CreateUserRequestDTO request(UserType type, String cpf, String cnpj) {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setUserType(type);
        dto.setCpf(cpf);
        dto.setCnpj(cnpj);
        return dto;
    }
}
