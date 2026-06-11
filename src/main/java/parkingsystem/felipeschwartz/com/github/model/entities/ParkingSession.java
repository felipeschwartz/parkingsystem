package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.enums.SessionStatus;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "parking_session")
public class ParkingSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Opcional: só quando for veículo cadastrado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    // Para avulso (ou para redundância/auditoria mesmo com vehicle cadastrado)
    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 20)
    private VehicleType vehicleType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "parking_space_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_parking_session_parking_space")
    )
    private ParkingSpace parkingSpace;

    @OneToOne(mappedBy = "parkingSession", cascade = CascadeType.ALL)
    private Payment payment;

    @Column
    private LocalDateTime entryTime;

    @Column
    private LocalDateTime exitTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column
    private BigDecimal amountCharged;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public ParkingSession() {
    }

    public ParkingSession(Long id, Vehicle vehicle, ParkingSpace parkingSpace,
                          LocalDateTime entryTime, LocalDateTime exitTime,
                          SessionStatus status, BigDecimal amountCharged) {
        this.id = id;
        this.vehicle = vehicle;
        this.parkingSpace = parkingSpace;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.status = status;
        this.amountCharged = amountCharged;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        if (vehicle != null) {
            this.licensePlate = vehicle.getLicensePlate();
            this.vehicleType = vehicle.getType();
        }
    }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSession that = (ParkingSession) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public void open(LocalDateTime entryTime) {
        Objects.requireNonNull(entryTime, "entryTime must not be null");

        if (this.status == SessionStatus.OPEN) {
            throw new IllegalStateException("the session is already open");
        }
        if (this.status == SessionStatus.CLOSED) {
            throw new IllegalStateException("the session is already closed");
        }
        this.entryTime = entryTime;
        this.exitTime = null;
        this.amountCharged = null;

        this.status = SessionStatus.OPEN;
        this.updatedAt = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = this.updatedAt;
    }

    public void close(LocalDateTime exitTime) {
        Objects.requireNonNull(exitTime, "exitTime must not be null");

        if (this.status != SessionStatus.OPEN) {
            throw new IllegalStateException("It's only possible to close the session, it is OPEN");
        }
        if (this.entryTime == null) {
            throw new IllegalStateException("entryTime not defined; impossible to close it");
        }
        if (exitTime.isBefore(this.entryTime)) {
            throw new IllegalArgumentException("exitTime can not be before entryTime");
        }

        this.exitTime = exitTime;
        this.status = SessionStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
    }

    public Duration getDuration() {
        if (this.entryTime == null) return Duration.ZERO;
        LocalDateTime end = (this.exitTime != null) ? this.exitTime : LocalDateTime.now();
        Duration d = Duration.between(this.entryTime, end);

        // Evita duration negativa em caso de dados inconsistentes
        return d.isNegative() ? Duration.ZERO : d;
    }

    public BigDecimal calculateAmount(BigDecimal ratePerHour) {
        Objects.requireNonNull(ratePerHour, "ratePerHour can not be null");
        if (ratePerHour.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("ratePerHour can not be negative");
        }
        if (this.status != SessionStatus.CLOSED) {
            throw new IllegalStateException("Calculating the value requires a CLOSED session");
        }

        Duration duration = getDuration();
        long minutes = duration.toMinutes(); // proporcional por minuto

        BigDecimal hours = BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 6, RoundingMode.HALF_UP);

        BigDecimal amount = ratePerHour.multiply(hours)
                .setScale(2, RoundingMode.HALF_UP);

        this.amountCharged = amount;
        this.updatedAt = LocalDateTime.now();
        return amount;
    }

    public static ParkingSession forHourly(
            String licensePlate,
            VehicleType vehicleType,
            ParkingSpace parkingSpace,
            LocalDateTime entryTime
    ) {
        Objects.requireNonNull(licensePlate, "licensePlate can not be null");
        Objects.requireNonNull(vehicleType, "vehicleType can not be null");
        Objects.requireNonNull(parkingSpace, "parkingSpace can not be null");
        Objects.requireNonNull(entryTime, "entryTime can not be null");

        ParkingSession s = new ParkingSession();
        s.vehicle = null;
        s.licensePlate = licensePlate;
        s.vehicleType = vehicleType;
        s.parkingSpace = parkingSpace;
        s.open(entryTime);
        return s;
    }


    public static ParkingSession forSubscription(
            Vehicle vehicle,
            ParkingSpace parkingSpace,
            LocalDateTime entryTime
    ) {
        Objects.requireNonNull(vehicle, "vehicle can not be null");
        Objects.requireNonNull(parkingSpace, "parkingSpace can not be null");
        Objects.requireNonNull(entryTime, "entryTime can not be null");

        ParkingSession s = new ParkingSession();
        s.vehicle = vehicle;
        s.licensePlate = vehicle.getLicensePlate();
        s.vehicleType = vehicle.getType();
        s.parkingSpace = parkingSpace;
        s.open(entryTime);
        return s;
    }
}
