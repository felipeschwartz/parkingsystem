package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class UserDTO<T extends UserDTO<T>> extends RepresentationModel<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String phone;
    private String email;
    private Set<VehicleDTO> vehicles = new HashSet<>();
    private AddressDTO address;
    private UserType userType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected UserDTO() {
    }

    public UserDTO(Long id, String phone, String email, AddressDTO address, UserType userType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.userType = userType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<VehicleDTO> getVehicles() { return vehicles; }
    public void setVehicles(Set<VehicleDTO> vehicles) { this.vehicles = vehicles; }

    public AddressDTO getAddress() { return address; }
    public void setAddress(AddressDTO address) { this.address = address; }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserDTO user)) return false;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}