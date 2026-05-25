package model.entities;

import java.util.Objects;

public class OwnerIndIvidual extends Owner {
    private String cpf;
    private String fullName;
    private String phone;
    private String email;

    public OwnerIndIvidual(Integer id) {
        super(id);
    }

    public OwnerIndIvidual(Integer id, String cpf, String fullName, String phone, String email) {
        super(id);
        this.cpf = cpf;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OwnerIndIvidual that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cpf);
    }
}
