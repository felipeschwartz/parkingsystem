package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.enums.SessionStatus;

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

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private ParkingSpace parkingSpace;

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
    }

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
        Objects.requireNonNull(entryTime, "entryTime não pode ser nulo");

        if (this.status == SessionStatus.OPEN) {
            throw new IllegalStateException("Sessão já está aberta");
        }
        if (this.status == SessionStatus.CLOSED) {
            throw new IllegalStateException("Sessão já está fechada e não pode ser reaberta");
        }
        this.entryTime = entryTime;
        this.exitTime = null;
        this.amountCharged = null;

        this.status = SessionStatus.OPEN;
        this.updatedAt = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = this.updatedAt;
    }

    public void close(LocalDateTime exitTime) {
        Objects.requireNonNull(exitTime, "exitTime não pode ser nulo");

        if (this.status != SessionStatus.OPEN) {
            throw new IllegalStateException("Só é possível fechar uma sessão com status OPEN");
        }
        if (this.entryTime == null) {
            throw new IllegalStateException("entryTime não definido; impossível fechar a sessão");
        }
        if (exitTime.isBefore(this.entryTime)) {
            throw new IllegalArgumentException("exitTime não pode ser anterior ao entryTime");
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
        Objects.requireNonNull(ratePerHour, "ratePerHour não pode ser nulo");
        if (ratePerHour.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("ratePerHour não pode ser negativo");
        }
        if (this.status != SessionStatus.CLOSED) {
            throw new IllegalStateException("Calcular valor exige sessão CLOSED");
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
}
