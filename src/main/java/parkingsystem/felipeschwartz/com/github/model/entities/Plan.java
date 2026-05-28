package model.entities;

import java.util.ArrayList;
import java.util.List;

public class Plan {
    private Long id;
    private String name;
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
}
