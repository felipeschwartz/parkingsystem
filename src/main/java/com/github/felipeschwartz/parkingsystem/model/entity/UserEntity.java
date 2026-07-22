package com.github.felipeschwartz.parkingsystem.model.entity;

import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_entities")
public class UserEntity extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column
    private String billingContact;

    @Column
    private String corporateName;

    @Column
    private String fantasyName;



    public UserEntity() {
    }

    public UserEntity(Long id, String phone, String email, Address address, UserType userType, UserProfile userProfile, LocalDateTime createdAt, LocalDateTime updatedAt, String password, String cnpj, String billingContact, String corporateName, String fantasyName) {
        super(id, phone, email, address, userType, userProfile, createdAt, updatedAt, password);
        this.cnpj = cnpj;
        this.billingContact = billingContact;
        this.corporateName = corporateName;
        this.fantasyName = fantasyName;
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


}
