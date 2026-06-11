package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingsystem.felipeschwartz.com.github.model.entities.SubscriptionContract;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.enums.SubscripionStatus;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerRepository;
import parkingsystem.felipeschwartz.com.github.services.exceptions.NoActiveContractException;
import parkingsystem.felipeschwartz.com.github.services.exceptions.OwnerNotFoundException;


@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }


    /**
     * Busca um veículo pela placa.
     * Lança OwnerNotFoundException se não encontrado.
     */
    @Transactional(readOnly = true)
    public Owner findOwnerById(Long id) {
        return ownerRepository
                .findOwnerById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));
        //      ^ Desembrulha o Optional — se vazio, lança a exceção
    }

     // Pesquisa um Owner pelo CPF
     @Transactional(readOnly = true)
     public Owner findOwnerIndividualByCPF(String cpf) {
         return ownerRepository
                 .findOwnerByCpf(cpf)                         // ← chama o repositório
                 .orElseThrow(() -> new OwnerNotFoundException("CPF: " + cpf));
     }

    @Transactional(readOnly = true)
    public Owner findOwnerEntityByCNPJ(String cnpj) {   // ← nome correto: CNPJ
        return ownerRepository
                .findOwnerByCnpj(cnpj)                       // ← chama o repositório
                .orElseThrow(() -> new OwnerNotFoundException("CNPJ: " + cnpj));
    }
}