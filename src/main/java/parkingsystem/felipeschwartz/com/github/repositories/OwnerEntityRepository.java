package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerEntity;

import java.util.Optional;

@Repository
public interface OwnerEntityRepository extends JpaRepository<OwnerEntity, Long> {
    Optional<OwnerEntity> findByCnpj(String cnpj);
}
