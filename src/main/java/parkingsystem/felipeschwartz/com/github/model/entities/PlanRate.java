package model.entities;

import model.enums.VehicleType;

import java.math.BigDecimal;


public class PlanRate {

    private Long id;
    private Plan plan;
    private VehicleType vehicleType;
    private BigDecimal monthlyPrice;

    public PlanRate() {
    }

    public PlanRate(Plan plan, VehicleType vehicleType, BigDecimal monthlyPrice) {
        this.plan = plan;
        this.vehicleType = vehicleType;
        this.monthlyPrice = monthlyPrice;
    }

    public Long getId() { return id; }
    public Plan getPlan() { return plan; }
    public VehicleType getVehicleType() { return vehicleType; }
    public BigDecimal getMonthlyPrice() { return monthlyPrice; }

    public void setPlan(Plan plan) { this.plan = plan; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    public void setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; }


    
}
