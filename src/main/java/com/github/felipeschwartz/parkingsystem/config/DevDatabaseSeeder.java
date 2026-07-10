package com.github.felipeschwartz.parkingsystem.config;

import com.github.felipeschwartz.parkingsystem.model.entity.*;
import com.github.felipeschwartz.parkingsystem.model.enums.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DevDatabaseSeeder implements CommandLineRunner {

    private static final String DEFAULT_USER_PASSWORD = "user123";

    @PersistenceContext
    private EntityManager em;

    private final PasswordEncoder passwordEncoder;

    public DevDatabaseSeeder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Seed idempotente: se já existe User, assume que já populou.
        if (count("User") > 0) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        String encodedDefaultPassword = passwordEncoder.encode(DEFAULT_USER_PASSWORD);

        // ---------- Helpers de enums (evita depender de nomes específicos) ----------
        VehicleType[] vehicleTypes = VehicleType.values();
        if (vehicleTypes.length == 0) throw new IllegalStateException("VehicleType enum has no values.");

        SpaceStatus[] spaceStatuses = SpaceStatus.values();
        ReservationStatus[] reservationStatuses = ReservationStatus.values();
        PaymentMethod[] paymentMethods = PaymentMethod.values();
        PaymentStatus[] paymentStatuses = PaymentStatus.values();

        // ---------- Addresses (embeddable; não persiste sozinho) ----------
        Address addr1 = new Address("Av. Paulista", "1000", "10º andar", "São Paulo", "SP", "01310-100", "BR");
        Address addr2 = new Address("Rua das Flores", "123", "Casa", "Curitiba", "PR", "80000-000", "BR");
        Address addr3 = new Address("Av. Atlântica", "500", "Apto 301", "Rio de Janeiro", "RJ", "22010-000", "BR");
        Address addr4 = new Address("Rua do Porto", "45", null, "Porto Alegre", "RS", "90000-000", "BR");
        Address addr5 = new Address("Av. Brasil", "777", "Sala 12", "Belo Horizonte", "MG", "30000-000", "BR");
        Address addr6 = new Address("Rua Central", "999", "Bloco B", "Florianópolis", "SC", "88000-000", "BR");

        // ---------- Users (3 Individuals + 3 Entities) ----------
        List<UserIndividual> individuals = new ArrayList<>();
        individuals.add(newIndividual("11911110001", "pf1@teste.com", addr1, UserType.USER, "11111111111", "Ana", "Silva", LocalDate.of(1994, 1, 10), encodedDefaultPassword, now));
        individuals.add(newIndividual("11911110002", "pf2@teste.com", addr2, UserType.USER, "22222222222", "Bruno", "Souza", LocalDate.of(1990, 5, 20), encodedDefaultPassword, now));
        individuals.add(newIndividual("11911110003", "pf3@teste.com", addr3, UserType.USER, "33333333333", "Carla", "Oliveira", LocalDate.of(1988, 9, 5), encodedDefaultPassword, now));


        List<UserEntity> entities = new ArrayList<>();
        entities.add(newEntity("11333330001", "pj1@teste.com", addr4, UserType.PARKING,"11111111000111", "Financeiro", "Empresa Alpha LTDA", "Alpha", encodedDefaultPassword, now));
        entities.add(newEntity("11333330002", "pj2@teste.com", addr5, UserType.PARKING_MANAGER, "22222222000122", "Contas", "Empresa Beta SA", "Beta", encodedDefaultPassword, now));
        entities.add(newEntity("11333330003", "pj3@teste.com", addr6, UserType.PARKING, "33333333000133", "Billing", "Empresa Gama ME", "Gama", encodedDefaultPassword, now));

        individuals.forEach(em::persist);
        entities.forEach(em::persist);
        em.flush();

        // Junta em lista "User" para facilitar
        List<User> users = new ArrayList<>();
        users.addAll(individuals);
        users.addAll(entities);

        // ---------- ParkingLots (3) ----------
        ParkingLot lot1 = new ParkingLot(null, "Lot Paulista", addr1, "113000-0001", 100, true, 60, 30, 10);
        lot1.setCreatedAt(now); lot1.setUpdatedAt(now);

        ParkingLot lot2 = new ParkingLot(null, "Lot Centro", addr2, "114000-0002", 80, true, 50, 20, 10);
        lot2.setCreatedAt(now); lot2.setUpdatedAt(now);

        ParkingLot lot3 = new ParkingLot(null, "Lot Praia", addr3, "115000-0003", 120, true, 70, 40, 10);
        lot3.setCreatedAt(now); lot3.setUpdatedAt(now);

        em.persist(lot1);
        em.persist(lot2);
        em.persist(lot3);
        em.flush();

        // ---------- ParkingSpaces (9 total: 3 por lot) ----------
        List<ParkingSpace> spaces = new ArrayList<>();
        spaces.add(newSpace(lot1, "1", "A01", vehicleTypes[0], spaceStatuses, now));
        spaces.add(newSpace(lot1, "1", "A02", vehicleTypes[Math.min(1, vehicleTypes.length - 1)], spaceStatuses, now));
        spaces.add(newSpace(lot1, "1", "A03", vehicleTypes[Math.min(2, vehicleTypes.length - 1)], spaceStatuses, now));

        spaces.add(newSpace(lot2, "2", "B01", vehicleTypes[0], spaceStatuses, now));
        spaces.add(newSpace(lot2, "2", "B02", vehicleTypes[Math.min(1, vehicleTypes.length - 1)], spaceStatuses, now));
        spaces.add(newSpace(lot2, "2", "B03", vehicleTypes[Math.min(2, vehicleTypes.length - 1)], spaceStatuses, now));

        spaces.add(newSpace(lot3, "3", "C01", vehicleTypes[0], spaceStatuses, now));
        spaces.add(newSpace(lot3, "3", "C02", vehicleTypes[Math.min(1, vehicleTypes.length - 1)], spaceStatuses, now));
        spaces.add(newSpace(lot3, "3", "C03", vehicleTypes[Math.min(2, vehicleTypes.length - 1)], spaceStatuses, now));

        spaces.forEach(em::persist);
        em.flush();

        // ---------- Plans (3) + PlanRates (9 total: 3 por plan) ----------
        Plan plan1 = new Plan(null, "Basic");
        plan1.setActive(true);
        plan1.setCreatedAt(now); plan1.setUpdatedAt(now);
        addRates(plan1, vehicleTypes, now,
                new BigDecimal("199.90"),
                new BigDecimal("299.90"),
                new BigDecimal("399.90"));

        Plan plan2 = new Plan(null, "Standard");
        plan2.setActive(true);
        plan2.setCreatedAt(now); plan2.setUpdatedAt(now);
        addRates(plan2, vehicleTypes, now,
                new BigDecimal("249.90"),
                new BigDecimal("349.90"),
                new BigDecimal("449.90"));

        Plan plan3 = new Plan(null, "Premium");
        plan3.setActive(true);
        plan3.setCreatedAt(now); plan3.setUpdatedAt(now);
        addRates(plan3, vehicleTypes, now,
                new BigDecimal("299.90"),
                new BigDecimal("399.90"),
                new BigDecimal("499.90"));

        plan1.validate();
        plan2.validate();
        plan3.validate();

        em.persist(plan1);
        em.persist(plan2);
        em.persist(plan3);
        em.flush();

        // ---------- HourlyRate (3) ----------
        HourlyRate hr1 = new HourlyRate(vehicleTypes[0], new BigDecimal("15.00"), true);
        hr1.setCreatedAt(now); hr1.setUpdatedAt(now);

        HourlyRate hr2 = new HourlyRate(vehicleTypes[Math.min(1, vehicleTypes.length - 1)], new BigDecimal("10.00"), true);
        hr2.setCreatedAt(now); hr2.setUpdatedAt(now);

        HourlyRate hr3 = new HourlyRate(vehicleTypes[Math.min(2, vehicleTypes.length - 1)], new BigDecimal("25.00"), true);
        hr3.setCreatedAt(now); hr3.setUpdatedAt(now);

        em.persist(hr1);
        em.persist(hr2);
        em.persist(hr3);
        em.flush();

        // ---------- Vehicles (6) ----------
        // Um veículo por user (6) => atende min 3 / max 10
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(newVehicle("ABC-1001", vehicleTypes[0], users.get(0), now));
        vehicles.add(newVehicle("ABC-1002", vehicleTypes[Math.min(1, vehicleTypes.length - 1)], users.get(1), now));
        vehicles.add(newVehicle("ABC-1003", vehicleTypes[Math.min(2, vehicleTypes.length - 1)], users.get(2), now));
        vehicles.add(newVehicle("XYZ-2001", vehicleTypes[0], users.get(3), now));
        vehicles.add(newVehicle("XYZ-2002", vehicleTypes[Math.min(1, vehicleTypes.length - 1)], users.get(4), now));
        vehicles.add(newVehicle("XYZ-2003", vehicleTypes[Math.min(2, vehicleTypes.length - 1)], users.get(5), now));

        vehicles.forEach(em::persist);
        em.flush();

        // ---------- SubscriptionContracts (3) ----------
        // 3 contratos vinculando (vehicle + user + plan)
        SubscriptionContract sc1 = new SubscriptionContract(null, vehicles.get(0), plan1, today.minusMonths(2), today.plusMonths(10), SubscripionStatus.ACTIVE, vehicles.get(0).getUser());
        sc1.setCreatedAt(now); sc1.setUpdatedAt(now);

        SubscriptionContract sc2 = new SubscriptionContract(null, vehicles.get(3), plan2, today.minusMonths(1), today.plusMonths(11), SubscripionStatus.ACTIVE, vehicles.get(3).getUser());
        sc2.setCreatedAt(now); sc2.setUpdatedAt(now);

        SubscriptionContract sc3 = new SubscriptionContract(null, vehicles.get(4), plan3, today.minusMonths(3), today.plusMonths(9), SubscripionStatus.ACTIVE, vehicles.get(4).getUser());
        sc3.setCreatedAt(now); sc3.setUpdatedAt(now);

        em.persist(sc1);
        em.persist(sc2);
        em.persist(sc3);
        em.flush();

        // ---------- Reservations (3) ----------
        Reservation r1 = new Reservation(null, vehicles.get(1), spaces.get(0), now.plusHours(1), now.plusHours(2), pick(reservationStatuses, 0));
        r1.setCreatedAt(now); r1.setUpdatedAt(now);

        Reservation r2 = new Reservation(null, vehicles.get(2), spaces.get(4), now.plusHours(3), now.plusHours(5), pick(reservationStatuses, 0));
        r2.setCreatedAt(now); r2.setUpdatedAt(now);

        Reservation r3 = new Reservation(null, vehicles.get(5), spaces.get(8), now.plusDays(1), now.plusDays(1).plusHours(2), pick(reservationStatuses, 0));
        r3.setCreatedAt(now); r3.setUpdatedAt(now);

        em.persist(r1);
        em.persist(r2);
        em.persist(r3);
        em.flush();

        // ---------- ParkingSessions (3) ----------
        ParkingSession s1 = ParkingSession.forSubscription(vehicles.get(0), spaces.get(1), now.minusHours(3));
        s1.close(now.minusHours(1));
        em.persist(s1);

        ParkingSession s2 = ParkingSession.forSubscription(vehicles.get(3), spaces.get(2), now.minusHours(5));
        s2.close(now.minusHours(2));
        em.persist(s2);

        ParkingSession s3 = ParkingSession.forHourly("HOUR-9999", vehicleTypes[0], spaces.get(3), now.minusHours(1));
        // deixa aberta (sem close)
        em.persist(s3);

        em.flush();

        // ---------- Payments (3) ----------
        // Payment NÃO tem @GeneratedValue no seu código; então setamos IDs manualmente.
        Payment p1 = new Payment(null, s1, new BigDecimal("0.00"), now.minusHours(1), pick(paymentMethods, 0), pick(paymentStatuses, 0), "PAY-0001", now, now);
        Payment p2 = new Payment(null, s2, new BigDecimal("0.00"), now.minusHours(2), pick(paymentMethods, 0), pick(paymentStatuses, 0), "PAY-0002", now, now);
        Payment p3 = new Payment(null, s3, new BigDecimal("15.00"), now, pick(paymentMethods, 0), pick(paymentStatuses, 0), "PAY-0003", now, now);

        em.persist(p1);
        em.persist(p2);
        em.persist(p3);

        em.flush();
    }

    // ----------------- helpers -----------------

    private long count(String entityName) {
        return em.createQuery("select count(e) from " + entityName + " e", Long.class)
                .getSingleResult();
    }

    private UserIndividual newIndividual(
            String phone, String email, Address address,
            UserType user, String cpf, String firstName, String lastName,
            LocalDate birthDate, String encodedPassword, LocalDateTime now
    ) {
        UserIndividual oi = new UserIndividual();
        oi.setPhone(phone);
        oi.setEmail(email);
        oi.setAddress(address);
        oi.setCpf(cpf);
        oi.setFirstName(firstName);
        oi.setLastName(lastName);
        oi.setBirthDate(birthDate);
        oi.setPassword(encodedPassword);
        oi.setCreatedAt(now);
        oi.setUpdatedAt(now);
        return oi;
    }

    private UserEntity newEntity(
            String phone, String email, Address address,
            UserType parking, String cnpj, String billingContact, String corporateName,
            String fantasyName, String encodedPassword, LocalDateTime now
    ) {
        UserEntity oe = new UserEntity();
        oe.setPhone(phone);
        oe.setEmail(email);
        oe.setAddress(address);
        oe.setCnpj(cnpj);
        oe.setBillingContact(billingContact);
        oe.setCorporateName(corporateName);
        oe.setFantasyName(fantasyName);
        oe.setPassword(encodedPassword);
        oe.setCreatedAt(now);
        oe.setUpdatedAt(now);
        return oe;
    }

    private ParkingSpace newSpace(
            ParkingLot lot, String floor, String position,
            VehicleType vehicleType, SpaceStatus[] statuses, LocalDateTime now
    ) {
        ParkingSpace ps = new ParkingSpace();
        ps.setParkingLot(lot);
        ps.setFloor(floor);
        ps.setPosition(position);
        ps.setVehicleType(vehicleType);
        ps.setStatus(pick(statuses, 0));
        ps.setActive(true);
        ps.setCreatedAt(now);
        ps.setUpdatedAt(now);
        return ps;
    }

    private Vehicle newVehicle(String plate, VehicleType type, User user, LocalDateTime now) {
        Vehicle v = new Vehicle();
        v.setLicensePlate(plate);
        v.setType(type);
        v.setUser(user);
        v.setCreatedAt(now);
        v.setUpdatedAt(now);
        return v;
    }

    private void addRates(Plan plan, VehicleType[] vehicleTypes, LocalDateTime now,
                          BigDecimal price1, BigDecimal price2, BigDecimal price3) {

        // Garante até 3 tipos (se o enum tiver menos que 3, repete o último disponível)
        VehicleType t1 = vehicleTypes[0];
        VehicleType t2 = vehicleTypes[Math.min(1, vehicleTypes.length - 1)];
        VehicleType t3 = vehicleTypes[Math.min(2, vehicleTypes.length - 1)];

        PlanRate r1 = new PlanRate(null, plan, t1, 1, price1, new BigDecimal("0"), true);
        r1.setCreatedAt(now); r1.setUpdatedAt(now);

        PlanRate r2 = new PlanRate(null, plan, t2, 3, price2, new BigDecimal("5"), true);
        r2.setCreatedAt(now); r2.setUpdatedAt(now);

        PlanRate r3 = new PlanRate(null, plan, t3, 12, price3, new BigDecimal("10"), true);
        r3.setCreatedAt(now); r3.setUpdatedAt(now);

        plan.getRates().add(r1);
        plan.getRates().add(r2);
        plan.getRates().add(r3);
    }

    private <T> T pick(T[] values, int index) {
        if (values == null || values.length == 0) return null;
        return values[Math.min(index, values.length - 1)];
    }
}