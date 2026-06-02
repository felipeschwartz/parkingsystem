package parkingsystem.felipeschwartz.com.github.model.entities;
import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "owner_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_vehicle_owner")
    )
    private Owner owner;

    @OneToMany(mappedBy = "vehicle")
    private List<SubscriptionContract> subscriptionContracts = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle")
    private List<ParkingSession> sessions = new ArrayList<>();

    public Vehicle() {
    }

    public Vehicle(Long id, String licensePlate, VehicleType type, Owner owner) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.type = type;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<SubscriptionContract> getSubscriptionContracts() {
        return subscriptionContracts;
    }

    public void setSubscriptionContracts(List<SubscriptionContract> subscriptionContracts) {
        this.subscriptionContracts = subscriptionContracts;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vehicle vehicle)) return false;
        return Objects.equals(getId(), vehicle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
