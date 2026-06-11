package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerEntity;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerIndividual;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerEntityRepository;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerIndividualRepository;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerRepository;
import parkingsystem.felipeschwartz.com.github.services.exceptions.OwnerNotFoundException;

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
    public Owner findOwnerById(Long id) {
        return ownerRepository
                .findOwnerById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));
    }

    public Optional<OwnerEntity> findOwnerEntityByCnpj(String cnpj) {
        return ownerEntityRepository.findByCnpj(cnpj);
    }

    public Optional<OwnerIndividual> findOwnerIndividualByCpf(String cpf) {
        return ownerIndividualRepository.findByCpf(cpf);
    }
}