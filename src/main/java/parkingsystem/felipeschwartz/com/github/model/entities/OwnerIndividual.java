package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "owner_individuals")
public class OwnerIndividual extends Owner implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    private String cpf;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private LocalDate birthDate;


    public OwnerIndividual() {
    }

    public OwnerIndividual(Long id, String phone, String email, String cpf, String firstName, String lastName, LocalDate birthDate, Address address) {
        super(id, phone, email, address);
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