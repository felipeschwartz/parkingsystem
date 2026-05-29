package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@DiscriminatorValue("entity")
public class OwnerEntity extends Owner implements Serializable {
    private static final long serialVersionUID = 1L;

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


}
