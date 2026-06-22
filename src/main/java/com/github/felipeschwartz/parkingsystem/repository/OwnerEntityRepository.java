package com.github.felipeschwartz.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerEntity;
import java.util.Optional;

@Repository
public interface OwnerEntityRepository extends JpaRepository<OwnerEntity, Long> {
    Optional<OwnerEntity> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);
}
