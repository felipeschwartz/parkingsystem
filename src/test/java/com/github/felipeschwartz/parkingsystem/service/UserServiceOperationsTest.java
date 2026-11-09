package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.mapper.UserCreationMapper;
import com.github.felipeschwartz.parkingsystem.mapper.UserEntityMapper;
import com.github.felipeschwartz.parkingsystem.mapper.UserIndividualMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.User;
import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import com.github.felipeschwartz.parkingsystem.repository.UserEntityRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserIndividualRepository;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceOperationsTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserIndividualRepository individualRepository = mock(UserIndividualRepository.class);
    private final UserEntityRepository entityRepository = mock(UserEntityRepository.class);
    private final UserEntityMapper entityMapper = mock(UserEntityMapper.class);
    private final UserIndividualMapper individualMapper = mock(UserIndividualMapper.class);
    private final UserCreationMapper creationMapper = mock(UserCreationMapper.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserService service = new UserService(userRepository, individualRepository, entityRepository,
            entityMapper, individualMapper, creationMapper, passwordEncoder);

    private UserIndividualDTO individualDto(Long id, String cpf) {
        return TestDtos.userIndividual(id, cpf);
    }

    private UserEntityDTO entityDto(Long id, String cnpj) {
        return TestDtos.userEntity(id, cnpj);
    }

    private UserIndividual individual(String cpf) {
        UserIndividual user = new UserIndividual();
        user.setUserType(UserType.INDIVIDUAL);
        user.setCpf(cpf);
        return user;
    }

    private UserEntity entity(String cnpj) {
        UserEntity user = new UserEntity();
        user.setUserType(UserType.ENTITY);
        user.setCnpj(cnpj);
        return user;
    }

    private CreateUserRequestDTO request(UserType type, String cpf, String cnpj) {
        return TestDtos.createUserRequest(type, cpf, cnpj);
    }

    private CreateUserRequestDTO request(UserType type) {
        return request(type, null, null);
    }

    @Test
    void findAllShouldMapEachUserByItsConcreteType() {
        UserIndividual person = individual("111");
        UserEntity company = entity("222");
        when(userRepository.findAll()).thenReturn(List.of(person, company));
        when(individualMapper.toDTO(person)).thenReturn(individualDto(1L, "111"));
        when(entityMapper.toDTO(company)).thenReturn(entityDto(2L, "222"));

        List<UserDTO> result = service.findAll();

        assertEquals(2, result.size());

    }

    @Test
    void findAllShouldRejectAnUnknownUserType() {
        User unknown = mock(User.class);
        when(userRepository.findAll()).thenReturn(List.of(unknown));

        assertThrows(IllegalStateException.class, () -> service.findAll());
    }

    @Test
    void findByIdShouldMapIndividualsAndEntities() {
        UserIndividual person = individual("111");
        UserEntity company = entity("222");
        when(userRepository.findById(1L)).thenReturn(Optional.of(person));
        when(userRepository.findById(2L)).thenReturn(Optional.of(company));
        when(individualMapper.toDTO(person)).thenReturn(individualDto(1L, "111"));
        when(entityMapper.toDTO(company)).thenReturn(entityDto(2L, "222"));

        assertTrue(service.findById(1L) instanceof UserIndividualDTO);
        assertTrue(service.findById(2L) instanceof UserEntityDTO);
    }

    @Test
    void findByIdShouldRejectAnUnknownUserType() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mock(User.class)));

        assertThrows(IllegalStateException.class, () -> service.findById(1L));
    }

    @Test
    void findByCpfShouldReturnTheMappedUser() {
        UserIndividual person = individual("111");
        when(individualRepository.findByCpf("111")).thenReturn(Optional.of(person));
        when(individualMapper.toDTO(person)).thenReturn(individualDto(1L, "111"));

        assertEquals("111", service.findByCpf("111").cpf());
    }

    @Test
    void findByCpfShouldFailWhenMissing() {
        when(individualRepository.findByCpf("111")).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findByCpf("111"));

        assertEquals("User with CPF not found: 111", e.getMessage());
    }

    @Test
    void findByCnpjShouldReturnTheMappedUser() {
        UserEntity company = entity("222");
        when(entityRepository.findByCnpj("222")).thenReturn(Optional.of(company));
        when(entityMapper.toDTO(company)).thenReturn(entityDto(2L, "222"));

        assertEquals("222", service.findByCnpj("222").cnpj());
    }

    @Test
    void findByCnpjShouldFailWhenMissing() {
        when(entityRepository.findByCnpj("222")).thenReturn(Optional.empty());

        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> service.findByCnpj("222"));

        assertEquals("User with CNPJ not found: 222", e.getMessage());
    }

    @Test
    void createShouldBuildAnIndividualWithEncodedPasswordRolesAndVehicles() {
        VehicleDTO vehicleDto = TestDtos.vehicle(null, "ABC1234");
        CreateUserRequestDTO request = TestDtos.createUserRequest(UserType.INDIVIDUAL, UserProfile.ADMIN, "111", null,
                Set.of(vehicleDto));
        UserIndividual user = individual("111");
        when(creationMapper.toIndividualEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(userRepository.save(user)).thenReturn(user);
        when(individualMapper.toDTO(user)).thenReturn(individualDto(1L, "111"));

        UserDTO result = service.create(request);

        assertEquals("encoded", user.getPassword());
        assertEquals(UserProfile.ADMIN.roles(), user.getRoles());
        assertNotNull(user.getCreatedAt());
        assertEquals(1, user.getVehicles().size());
        Vehicle vehicle = user.getVehicles().iterator().next();
        assertEquals("ABC1234", vehicle.getLicensePlate());
        assertSame(user, vehicle.getUser());
    }

    @Test
    void createShouldBuildAnEntityUser() {
        CreateUserRequestDTO request = request(UserType.ENTITY, null, "222");
        UserEntity user = entity("222");
        when(creationMapper.toEntityEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(entityMapper.toDTO(user)).thenReturn(entityDto(2L, "222"));

        UserDTO result = service.create(request);

        assertTrue(result instanceof UserEntityDTO);
        assertTrue(user.getVehicles() == null || user.getVehicles().isEmpty());
    }

    @Test
    void createShouldRejectAnUnsupportedUserType() {
        assertThrows(IllegalArgumentException.class, () -> service.create(request(null)));
    }

    @Test
    void createShouldRejectASavedUserOfAnUnknownType() {
        CreateUserRequestDTO request = request(UserType.INDIVIDUAL, "111", null);
        UserIndividual user = individual("111");
        when(creationMapper.toIndividualEntity(request)).thenReturn(user);
        doReturn(mock(User.class)).when(userRepository).save(user);

        assertThrows(IllegalStateException.class, () -> service.create(request));
    }

    @Test
    void updateShouldApplyChangesOnAnIndividual() {
        UserIndividual user = individual("111");
        CreateUserRequestDTO request = request(UserType.INDIVIDUAL, "999", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(individualRepository.existsByCpf("999")).thenReturn(false);
        when(individualRepository.save(user)).thenReturn(user);
        when(individualMapper.toDTO(user)).thenReturn(individualDto(1L, "999"));

        UserDTO result = service.update(1L, request);

        verify(individualMapper).updateIndividualFromCreateRequest(request, user);
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void updateShouldRejectACpfOwnedByAnotherUser() {
        UserIndividual user = individual("111");
        CreateUserRequestDTO request = request(UserType.INDIVIDUAL, "999", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(individualRepository.existsByCpf("999")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.update(1L, request));
        verify(individualRepository, never()).save(any());
    }

    @Test
    void updateShouldAllowKeepingTheSameCpf() {
        UserIndividual user = individual("111");
        CreateUserRequestDTO request = request(UserType.INDIVIDUAL, "111", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(individualRepository.save(user)).thenReturn(user);
        when(individualMapper.toDTO(user)).thenReturn(individualDto(1L, "111"));

        service.update(1L, request);

        verify(individualRepository, never()).existsByCpf(any());
    }

    @Test
    void updateShouldApplyChangesOnAnEntityUser() {
        UserEntity user = entity("222");
        CreateUserRequestDTO request = request(UserType.ENTITY, null, "888");
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(entityRepository.existsByCnpj("888")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(entityMapper.toDTO(user)).thenReturn(entityDto(2L, "888"));

        UserDTO result = service.update(2L, request);

        verify(entityMapper).updateEntityFromCreateRequest(request, user);
    }

    @Test
    void updateShouldRejectACnpjOwnedByAnotherUser() {
        UserEntity user = entity("222");
        CreateUserRequestDTO request = request(UserType.ENTITY, null, "888");
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(entityRepository.existsByCnpj("888")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.update(2L, request));
    }

    @Test
    void updateShouldRejectChangingTheUserType() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(individual("111")));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.update(1L, request(UserType.ENTITY)));

        assertTrue(e.getMessage().startsWith("Cannot change user type during update."));
    }

    @Test
    void updateShouldRejectAnUnknownConcreteUser() {
        User unknown = mock(User.class);
        when(unknown.getUserType()).thenReturn(UserType.INDIVIDUAL);
        when(userRepository.findById(1L)).thenReturn(Optional.of(unknown));

        assertThrows(IllegalStateException.class, () -> service.update(1L, request(UserType.INDIVIDUAL)));
    }

    @Test
    void updateShouldFailWhenUserIsMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> service.update(1L, request(UserType.INDIVIDUAL)));
    }

    @Test
    void updateShouldRejectNullRequest() {
        assertThrows(NullPointerException.class, () -> service.update(1L, null));
    }

    @Test
    void deleteShouldRemoveExistingUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteShouldFailWhenMissing() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> service.delete(1L));
        verify(userRepository, never()).deleteById(any());
    }
}
