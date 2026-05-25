package model.entities;

import java.util.Objects;

public class OwnerEntity extends Owner {
    private String cnpj;
    private String fullName;
    private String phone;
    private String email;
    private String billingContact;

    public OwnerEntity(Integer id) {
        super(id);
    }

    public OwnerEntity(Integer id, String cnpj, String fullName, String phone, String email, String billingContact) {
        super(id);
        this.cnpj = cnpj;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.billingContact = billingContact;
    }

    public String getCpf() {
        return cnpj;
    }

    public void setCpf(String cnpj) {
        this.cnpj = cnpj;
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

    public String getBillingContact() {
        return billingContact;
    }

    public void setBillingContact(String billingContact) {
        this.billingContact = billingContact;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OwnerEntity that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(cnpj, that.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cnpj);
    }
}
