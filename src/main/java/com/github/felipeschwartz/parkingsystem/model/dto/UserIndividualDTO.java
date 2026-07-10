package com.github.felipeschwartz.parkingsystem.model.dto;

import java.time.LocalDate;

public class UserIndividualDTO extends UserDTO<UserIndividualDTO> {
    private String cpf;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public UserIndividualDTO() {
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
