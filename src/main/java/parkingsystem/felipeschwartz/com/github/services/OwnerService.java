package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerDTO;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerEntityDTO;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerIndividualDTO;
import parkingsystem.felipeschwartz.com.github.mapper.OwnerEntityMapper;
import parkingsystem.felipeschwartz.com.github.mapper.OwnerIndividualMapper;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerEntity;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerIndividual;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerEntityRepository;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerIndividualRepository;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerRepository;
import parkingsystem.felipeschwartz.com.github.services.exceptions.ObjectNotFoundException;

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

    @Autowired
    private OwnerIndividualMapper individualMapper;

    @Autowired
    private OwnerEntityMapper entityMapper;

    @Transactional(readOnly = true)
    public List<OwnerDTO> findAll() {
        return ownerRepository.findAll()
                .stream()
                .map(owner -> {
                    if (owner instanceof OwnerIndividual oi) {
                        return (OwnerDTO) individualMapper.toDTO(oi);
                    } else if (owner instanceof OwnerEntity oe) {
                        return (OwnerDTO) entityMapper.toDTO(oe);
                    }
                    throw new IllegalStateException("Unknown owner type: " + owner.getClass());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public OwnerDTO findOwnerById(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ObjectNotFoundException("Owner not found: " + ownerId));

        if (owner instanceof OwnerIndividual oi) {
            return individualMapper.toDTO(oi);
        } else if (owner instanceof OwnerEntity oe) {
            return entityMapper.toDTO(oe);
        }
        throw new IllegalStateException("Unknown owner type: " + owner.getClass());
    }

    @Transactional(readOnly = true)
    public OwnerIndividualDTO findOwnerByCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF must not be blank.");
        }
        return ownerIndividualRepository.findByCpf(cpf)
                .map(individualMapper::toDTO)
                .orElseThrow(() -> new ObjectNotFoundException("Owner not found: " + cpf));
    }

    @Transactional(readOnly = true)
    public OwnerEntityDTO findOwnerByCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ must not be blank.");
        }
        return ownerEntityRepository.findByCnpj(cnpj)
                .map(entityMapper::toDTO)
                .orElseThrow(() -> new ObjectNotFoundException("Owner not found: " + cnpj));
    }

    // -------- CREATE --------

    @Transactional
    public OwnerIndividualDTO createIndividual(OwnerIndividual individual) {
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

        return individualMapper.toDTO(ownerIndividualRepository.save(individual));
    }

    @Transactional
    public OwnerEntityDTO createEntity(OwnerEntity entity) {
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

        return entityMapper.toDTO(ownerEntityRepository.save(entity));
    }


    // -------- UPDATE --------

    @Transactional
    public OwnerIndividualDTO updateIndividual(Long id, OwnerIndividual updated) {
        OwnerIndividual individual = ownerIndividualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + id));

        if (updated.getPhone() != null) individual.setPhone(updated.getPhone());
        if (updated.getEmail() != null) individual.setEmail(updated.getEmail());
        if (updated.getFirstName() != null) individual.setFirstName(updated.getFirstName());
        if (updated.getLastName() != null) individual.setLastName(updated.getLastName());
        if (updated.getBirthDate() != null) individual.setBirthDate(updated.getBirthDate());
        if (updated.getAddress() != null) individual.setAddress(updated.getAddress());

        individual.setUpdatedAt(LocalDateTime.now());
        return individualMapper.toDTO(ownerIndividualRepository.save(individual));
    }

    @Transactional
    public OwnerEntityDTO updateEntity(Long id, OwnerEntity updated) {
        OwnerEntity entity = ownerEntityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + id));

        if (updated.getPhone() != null) entity.setPhone(updated.getPhone());
        if (updated.getEmail() != null) entity.setEmail(updated.getEmail());
        if (updated.getBillingContact() != null) entity.setBillingContact(updated.getBillingContact());
        if (updated.getCorporateName() != null) entity.setCorporateName(updated.getCorporateName());
        if (updated.getFantasyName() != null) entity.setFantasyName(updated.getFantasyName());
        if (updated.getAddress() != null) entity.setAddress(updated.getAddress());

        entity.setUpdatedAt(LocalDateTime.now());
        return entityMapper.toDTO(ownerEntityRepository.save(entity));
    }


    // -------- DELETE --------

    @Transactional
    public void delete(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + ownerId));
        ownerRepository.delete(owner);
    }
}