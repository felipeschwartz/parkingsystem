package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "plan")
public class Plan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private List<PlanRate> rates = new ArrayList<>();

    public Plan() {
    }

    public Plan(Long id, String name) {
        this.id = id;
        this.name = name;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlanRate> getRates() {
        return rates;
    }

    public void setRates(List<PlanRate> rates) {
        this.rates = rates;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Plan plan)) return false;
        return Objects.equals(getId(), plan.getId()) && Objects.equals(getName(), plan.getName()) && Objects.equals(getRates(), plan.getRates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRates());
    }

    @OneToMany(mappedBy = "plan")
    private Collection<SubscriptionContract> subscriptionContract;

    public Collection<SubscriptionContract> getSubscriptionContract() {
        return subscriptionContract;
    }

    public void setSubscriptionContract(Collection<SubscriptionContract> subscriptionContract) {
        this.subscriptionContract = subscriptionContract;
    }
}
