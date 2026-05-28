package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
@DiscriminatorValue("individual")
public class OwnerIndIvidual extends Owner {
    private String cpf;

    public OwnerIndIvidual(String cpf) {
        this.cpf = cpf;
    }

    protected OwnerIndIvidual() {}

    public OwnerIndIvidual(String phone, String email, String fullName, String cpf) {
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
        if (!(o instanceof OwnerIndIvidual that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getCpf(), that.getCpf());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCpf());
    }
}