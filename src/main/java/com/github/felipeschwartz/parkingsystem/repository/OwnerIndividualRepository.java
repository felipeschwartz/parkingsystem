package com.github.felipeschwartz.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerIndividual;
import java.util.Optional;

@Repository
public interface OwnerIndividualRepository extends JpaRepository<OwnerIndividual, Long> {
    Optional<OwnerIndividual> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
