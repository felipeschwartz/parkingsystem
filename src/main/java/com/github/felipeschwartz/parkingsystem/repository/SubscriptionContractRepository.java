package com.github.felipeschwartz.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionContractRepository extends JpaRepository<SubscriptionContract, Long> {

    // Query method simples: busca por veículo e status (sem lógica de vigência)
    List<SubscriptionContract> findByVehicleIdAndStatusOrderByStartDateDesc(Long vehicleId, SubscripionStatus status);
    Optional<SubscriptionContract> findByVehicleAndStatus(Vehicle vehicle, SubscripionStatus status);

    // @Query: contrato ATIVO e vigente numa data (cobre endDate null)
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

    // @Query: detetar sobreposição de período (para impedir contratos duplicados)
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
          and sc.status = :subscription_status
          and sc.startDate <= :today
          and (sc.endDate is null or sc.endDate >= :today)
    """)
    boolean hasActiveContractForDate(@Param("vehicleId") Long vehicleId,
                                     @Param("subscription_status")  SubscripionStatus status,
                                     @Param("today") LocalDate today);
}
