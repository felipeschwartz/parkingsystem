package com.github.felipeschwartz.parkingsystem.model.dto;

import org.springframework.hateoas.RepresentationModel;
import com.github.felipeschwartz.parkingsystem.model.entity.Address;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class OwnerDTO extends RepresentationModel<OwnerDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String phone;
    private String email;
    private Set<Vehicle> vehicles = new HashSet<>();
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected OwnerDTO() {
    }

    public OwnerDTO(Long id, String phone, String email, Address address) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Vehicle> getVehicles() { return vehicles; }
    public void setVehicles(Set<Vehicle> vehicles) { this.vehicles = vehicles; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OwnerDTO owner)) return false;
        return Objects.equals(getId(), owner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}