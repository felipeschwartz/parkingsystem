package parkingsystem.felipeschwartz.com.github.data.dto;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import parkingsystem.felipeschwartz.com.github.model.entities.Address;
import parkingsystem.felipeschwartz.com.github.model.entities.ParkingSpace;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ParkingLotDTO extends RepresentationModel<ParkingLotDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String parkingLotName;
    private Address address;
    private String phoneNumber;
    private Integer totalSpaces;
    private Boolean active;
    private Set<ParkingSpace> parkingSpaces = new HashSet<>();
    private Integer carSpaces;
    private Integer motorcycleSpaces;
    private Integer truckSpaces;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ParkingLotDTO() {
    }

    public ParkingLotDTO(Long id, String parkingLotName, Address address, String phoneNumber, Integer totalSpaces, Boolean active, Integer carSpaces, Integer motorcycleSpaces, Integer truckSpaces) {
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

    public Set<ParkingSpace> getParkingSpaces() {

        return parkingSpaces;
    }

    public void setParkingSpaces(Set<ParkingSpace> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
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
        ParkingLotDTO that = (ParkingLotDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
