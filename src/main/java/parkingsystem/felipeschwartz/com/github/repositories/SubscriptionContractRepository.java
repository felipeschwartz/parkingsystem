package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import parkingsystem.felipeschwartz.com.github.model.entities.SubscriptionContract;
import parkingsystem.felipeschwartz.com.github.model.enums.SubscripionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionContractRepository extends JpaRepository<SubscriptionContract, Long> {

    // Query method simples: busca por veículo e status (sem lógica de vigência)
    List<SubscriptionContract> findByVehicleIdAndStatusOrderByStartDateDesc(Long vehicleId, SubscripionStatus status);


    // @Query: contrato ATIVO e vigente em uma data (cobre endDate null)
    @Query("""
           select sc
           from SubscriptionContract sc
           where sc.vehicle.id = :vehicleId
             and sc.status = :status
             and sc.startDate <= :date
             and (sc.endDate is null or sc.endDate >= :date)
           order by sc.startDate desc
           """)
    Optional<SubscriptionContract> findActiveOnDate(
            @Param("vehicleId") Long vehicleId,
            @Param("status") SubscripionStatus status,
            @Param("date") LocalDate date
    );

    // @Query: detectar sobreposição de período (para impedir contratos duplicados)
    @Query("""
           select sc
           from SubscriptionContract sc
           where sc.vehicle.id = :vehicleId
             and sc.status = :status
             and sc.startDate <= :endDate
             and (sc.endDate is null or sc.endDate >= :startDate)
           """)
    List<SubscriptionContract> findOverlappingContracts(
            @Param("vehicleId") Long vehicleId,
            @Param("status") SubscripionStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query("""
        select (count(sc) > 0)
        from SubscriptionContract sc
        where sc.vehicle.id = :vehicleId
          and sc.status = 'ACTIVE'
          and :today between sc.startDate and sc.endDate
    """)
    boolean hasActiveContractForDate(@Param("vehicleId") Long vehicleId,
                                     @Param("today") LocalDate today);
}
