package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingsystem.felipeschwartz.com.github.model.entities.*;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerEntityRepository;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerIndividualRepository;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerRepository;
import parkingsystem.felipeschwartz.com.github.services.exceptions.OwnerNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerIndividualRepository ownerIndividualRepository;
    private final OwnerEntityRepository ownerEntityRepository;

    public OwnerService(OwnerRepository ownerRepository, OwnerIndividualRepository ownerIndividualRepository, OwnerEntityRepository ownerEntityRepository) {
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
                .orElseThrow(() -> new IllegalArgumentException("Owner não encontrado: " + ownerId));
    }

    @Transactional(readOnly = true)
    public OwnerIndividual findOwnerByCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) throw new IllegalArgumentException("cpf não pode ser vazio.");
        return ownerIndividualRepository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("OwnerIndividual não encontrado para cpf: " + cpf));
    }

    @Transactional(readOnly = true)
    public OwnerEntity findOwnerByCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) throw new IllegalArgumentException("cnpj não pode ser vazio.");
        return ownerEntityRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new IllegalArgumentException("OwnerEntity não encontrado para cnpj: " + cnpj));
    }

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
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("cpf não pode ser vazio.");
        }
        if (ownerIndividualRepository.findByCpf(cpf).isPresent()) {
            throw new IllegalStateException("Já existe OwnerIndividual com este cpf.");
        }

        OwnerIndividual oi = new OwnerIndividual();
        oi.setPhone(phone);
        oi.setEmail(email);
        // Owner tem Address mas no seu código não há getter/setter expostos.
        // Se você tiver setAddress/getAddress, descomente a linha abaixo:
        // oi.setAddress(address);

        oi.setCpf(cpf);
        oi.setFirstName(firstName);
        oi.setLastName(lastName);
        oi.setBirthDate(birthDate);

        LocalDateTime now = LocalDateTime.now();
        oi.setCreatedAt(now);
        oi.setUpdatedAt(now);

        return ownerIndividualRepository.save(oi);
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
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("cnpj não pode ser vazio.");
        }
        if (ownerEntityRepository.findByCnpj(cnpj).isPresent()) {
            throw new IllegalStateException("Já existe OwnerEntity com este cnpj.");
        }

        OwnerEntity oe = new OwnerEntity();
        oe.setPhone(phone);
        oe.setEmail(email);
        // Se você tiver setAddress/getAddress em Owner, descomente:
        // oe.setAddress(address);

        oe.setCnpj(cnpj);
        oe.setBillingContact(billingContact);
        oe.setCorporateName(corporateName);
        oe.setFantasyName(fantasyName);

        LocalDateTime now = LocalDateTime.now();
        oe.setCreatedAt(now);
        oe.setUpdatedAt(now);

        return ownerEntityRepository.save(oe);
    }

    public Owner update(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId.getId()).orElseThrow(() -> new RuntimeException("No records found for this ID"));
        owner.setVehicleType(owner.getVehicleType());
        owner.setRatePerHour(owner.getRatePerHour());
        return ownerRepository.save(owner);
    }

    public void delete(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("No records found for this ID"));
        ownerRepository.delete(owner);
    }

    public Optional<OwnerEntity> findOwnerEntityByCnpj(String cnpj) {
        return ownerEntityRepository.findByCnpj(cnpj);
    }

    public Optional<OwnerIndividual> findOwnerIndividualByCpf(String cpf) {
        return ownerIndividualRepository.findByCpf(cpf);
    }
}