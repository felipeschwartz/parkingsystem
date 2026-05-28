package model.entities;

import model.enums.ContractStatus;

import java.time.LocalDate;

public class SubscriptionContract {
    private Long id;
    private Vehicle vehicle;
    private Plan plan;
    private ContractStatus status = ContractStatus.ACTIVE;
    private LocalDate startDate;
    private LocalDate endDate;
    private Owner owner;

    public SubscriptionContract() {}

    public SubscriptionContract(Vehicle vehicle, Plan plan, LocalDate startDate, LocalDate endDate) {
        this.vehicle = vehicle;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ContractStatus.ACTIVE;
    }

    public Long getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public Plan getPlan() { return plan; }
    public ContractStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public void setPlan(Plan plan) { this.plan = plan; }
    public void setStatus(ContractStatus status) { this.status = status; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isActiveOn(LocalDate date) {
        if (status != ContractStatus.ACTIVE) return false;
        boolean afterStart = !date.isBefore(startDate);
        boolean beforeEnd = (endDate == null) || !date.isAfter(endDate);
        return afterStart && beforeEnd;
    }
}
