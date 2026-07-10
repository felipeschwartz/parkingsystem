package com.github.felipeschwartz.parkingsystem.repository;

import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserIndividualRepository extends JpaRepository<UserIndividual, Long> {
    Optional<UserIndividual> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
