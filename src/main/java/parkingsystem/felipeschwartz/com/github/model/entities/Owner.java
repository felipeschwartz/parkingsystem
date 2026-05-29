package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "owner")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "owner_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Owner implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private String fullName;

    protected Owner() {
    }

    protected Owner(String phone, String email, String fullName) {
        this.phone = phone;
        this.email = email;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Owner owner)) return false;
        return Objects.equals(getId(), owner.getId()) && Objects.equals(getPhone(), owner.getPhone()) && Objects.equals(getEmail(), owner.getEmail()) && Objects.equals(getFullName(), owner.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPhone(), getEmail(), getFullName());
    }
}
