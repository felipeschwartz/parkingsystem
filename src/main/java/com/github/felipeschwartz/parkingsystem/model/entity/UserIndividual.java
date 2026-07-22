package com.github.felipeschwartz.parkingsystem.model.entity;

import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_individuals")
public class UserIndividual extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private LocalDate birthDate;


    public UserIndividual() {
    }

    public UserIndividual(Long id, String phone, String email, Address address, UserType userType, UserProfile userProfile, LocalDateTime createdAt, LocalDateTime updatedAt, String password, String cpf, String firstName, String lastName, LocalDate birthDate) {
        super(id, phone, email, address, userType, userProfile, createdAt, updatedAt, password);
        this.cpf = cpf;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}