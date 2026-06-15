package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerIndividual;
import java.util.Optional;

@Repository
public interface OwnerIndividualRepository extends JpaRepository<OwnerIndividual, Long> {
    Optional<OwnerIndividual> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
