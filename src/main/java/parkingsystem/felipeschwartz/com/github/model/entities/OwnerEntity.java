package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@DiscriminatorValue("entity")
public class OwnerEntity extends Owner {
    @Column
    private String cnpj;

    @Column
    private String billingContact;

    protected OwnerEntity() {
    }

    public OwnerEntity(String phone, String email, String fullName,
                       String cnpj, String billingContact) {
        super(phone, email, fullName);
        this.cnpj = cnpj;
        this.billingContact = billingContact;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
        return Objects.equals(getCnpj(), that.getCnpj()) && Objects.equals(getBillingContact(), that.getBillingContact());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCnpj(), getBillingContact());
    }
}
