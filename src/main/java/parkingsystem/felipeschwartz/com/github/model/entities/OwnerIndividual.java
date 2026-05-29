package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Objects;

@Entity
@DiscriminatorValue("individual")
public class OwnerIndividual extends Owner implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    private String cpf;

    public OwnerIndividual(String cpf) {
        this.cpf = cpf;
    }

    protected OwnerIndividual() {}

    public OwnerIndividual(String phone, String email, String fullName, String cpf) {
        super(phone, email, fullName);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OwnerIndividual that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getCpf(), that.getCpf());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCpf());
    }
}