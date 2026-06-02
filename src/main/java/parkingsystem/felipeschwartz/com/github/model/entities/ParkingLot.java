package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String parkingLotName;

    @Embedded
    private Address address;

    @Column
    private String phoneNumber;

    @Column
    private Integer totalSpaces;

    @Column
    private Boolean active;

    @Column
    private Integer carSpaces;

    @Column
    private Integer motorcycleSpaces;

    @Column
    private Integer truckSpaces;

    public ParkingLot() {
    }

    public ParkingLot(Long id, String parkingLotName, Address address, String phoneNumber, Integer totalSpaces, Boolean active, Integer carSpaces, Integer motorcycleSpaces, Integer truckSpaces) {
        this.id = id;
        this.parkingLotName = parkingLotName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.totalSpaces = totalSpaces;
        this.active = active;
        this.carSpaces = carSpaces;
        this.motorcycleSpaces = motorcycleSpaces;
        this.truckSpaces = truckSpaces;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long parkingLotId) {
        this.id = parkingLotId;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getTotalSpaces() {
        return totalSpaces;
    }

    public void setTotalSpaces(Integer totalSpaces) {
        this.totalSpaces = totalSpaces;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getCarSpaces() {
        return carSpaces;
    }

    public void setCarSpaces(Integer carSpaces) {
        this.carSpaces = carSpaces;
    }

    public Integer getMotorcycleSpaces() {
        return motorcycleSpaces;
    }

    public void setMotorcycleSpaces(Integer motorcycleSpaces) {
        this.motorcycleSpaces = motorcycleSpaces;
    }

    public Integer getTruckSpaces() {
        return truckSpaces;
    }

    public void setTruckSpaces(Integer truckSpaces) {
        this.truckSpaces = truckSpaces;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLot that = (ParkingLot) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
