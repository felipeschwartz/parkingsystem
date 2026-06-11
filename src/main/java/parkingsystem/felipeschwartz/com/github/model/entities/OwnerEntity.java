package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "owner_entities")
public class OwnerEntity extends Owner implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    private String cnpj;

    @Column
    private String billingContact;

    @Column
    private String corporateName;

    @Column
    private String fantasyName;

    @Embedded
    private Address address;

    protected OwnerEntity() {
    }

    public OwnerEntity(String phone, String email, String cnpj, String billingContact, String corporateName, String fantasyName, Address address) {
        super(phone, email);
        this.cnpj = cnpj;
        this.billingContact = billingContact;
        this.corporateName = corporateName;
        this.fantasyName = fantasyName;
        this.address = address;
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

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
