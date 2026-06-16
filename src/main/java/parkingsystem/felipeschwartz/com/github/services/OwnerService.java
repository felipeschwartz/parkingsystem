package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingsystem.felipeschwartz.com.github.model.entities.Address;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerEntity;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerIndividual;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerEntityRepository;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerIndividualRepository;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerIndividualRepository ownerIndividualRepository;
    private final OwnerEntityRepository ownerEntityRepository;

    public OwnerService(
            OwnerRepository ownerRepository,
            OwnerIndividualRepository ownerIndividualRepository,
            OwnerEntityRepository ownerEntityRepository
    ) {
        this.ownerRepository = ownerRepository;
        this.ownerIndividualRepository = ownerIndividualRepository;
        this.ownerEntityRepository = ownerEntityRepository;
    }

    @Transactional(readOnly = true)
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Owner findOwnerById(Long ownerId) {
        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + ownerId));
    }

    @Transactional(readOnly = true)
    public OwnerIndividual findOwnerByCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF must not be blank.");
        }
        return ownerIndividualRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("OwnerIndividual not found for CPF: " + cpf));
    }

    @Transactional(readOnly = true)
    public OwnerEntity findOwnerByCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ must not be blank.");
        }
        return ownerEntityRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new IllegalArgumentException("OwnerEntity not found for CNPJ: " + cnpj));
    }

    // -------- CREATE --------

    @Transactional
    public OwnerIndividual createIndividual(OwnerIndividual individual) {
        Objects.requireNonNull(individual, "individual must not be null.");

        String cpf = individual.getCpf();
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF must not be blank.");
        }
        if (ownerIndividualRepository.existsByCpf(cpf)) {
            throw new IllegalStateException("An OwnerIndividual with this CPF already exists.");
        }

        LocalDateTime now = LocalDateTime.now();
        individual.setCreatedAt(now);
        individual.setUpdatedAt(now);

        return ownerIndividualRepository.save(individual);
    }

    @Transactional
    public OwnerEntity createEntity(OwnerEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null.");

        String cnpj = entity.getCnpj();
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ must not be blank.");
        }
        if (ownerEntityRepository.existsByCnpj(cnpj)) {
            throw new IllegalStateException("An OwnerEntity with this CNPJ already exists.");
        }

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return ownerEntityRepository.save(entity);
    }

    // -------- CREATE (field-based) --------

    @Transactional
    public OwnerIndividual createIndividual(
            String phone,
            String email,
            Address address,
            String cpf,
            String firstName,
            String lastName,
            LocalDate birthDate
    ) {
        OwnerIndividual oi = new OwnerIndividual();
        oi.setPhone(phone);
        oi.setEmail(email);
        oi.setAddress(address);
        oi.setCpf(cpf);
        oi.setFirstName(firstName);
        oi.setLastName(lastName);
        oi.setBirthDate(birthDate);

        return createIndividual(oi);
    }

    @Transactional
    public OwnerEntity createEntity(
            String phone,
            String email,
            Address address,
            String cnpj,
            String billingContact,
            String corporateName,
            String fantasyName
    ) {
        OwnerEntity oe = new OwnerEntity();
        oe.setPhone(phone);
        oe.setEmail(email);
        oe.setAddress(address);
        oe.setCnpj(cnpj);
        oe.setBillingContact(billingContact);
        oe.setCorporateName(corporateName);
        oe.setFantasyName(fantasyName);

        return createEntity(oe);
    }

    // -------- UPDATE --------

    @Transactional
    public OwnerIndividual updateIndividual(Long id, String phone, String email, String cpf, String firstName, String lastName, LocalDate birthDate, Address address) {
        OwnerIndividual individual = ownerIndividualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + id));

        if (phone != null) individual.setPhone(phone);
        if (email != null) individual.setEmail(email);
        if (firstName != null) individual.setFirstName(firstName);
        if (lastName != null) individual.setLastName(lastName);
        if (birthDate != null) individual.setBirthDate(birthDate);
        if (address != null) individual.setAddress(address);

        individual.setUpdatedAt(LocalDateTime.now());
        return ownerRepository.save(individual);
    }

    @Transactional
    public OwnerEntity updateEntity(Long id, String phone, String email, String cnpj, String billingContact, String corporateName, String fantasyName, Address address) {
        OwnerEntity entity = ownerEntityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + id));

        if (phone != null) entity.setPhone(phone);
        if (email != null) entity.setEmail(email);
        if (billingContact != null) entity.setBillingContact(billingContact);
        if (corporateName != null) entity.setCorporateName(corporateName);
        if (fantasyName != null) entity.setFantasyName(fantasyName);
        if (address != null) entity.setAddress(address);

        entity.setUpdatedAt(LocalDateTime.now());
        return ownerRepository.save(entity);
    }


    // -------- DELETE --------

    @Transactional
    public void delete(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + ownerId));
        ownerRepository.delete(owner);
    }
}