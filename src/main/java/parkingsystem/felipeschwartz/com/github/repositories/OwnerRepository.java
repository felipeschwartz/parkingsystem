package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerDTO;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);
}