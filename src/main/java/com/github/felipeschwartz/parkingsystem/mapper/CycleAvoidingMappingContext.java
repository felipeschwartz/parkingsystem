package com.github.felipeschwartz.parkingsystem.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Gives the default methods of {@link PlanMapper} access to the other mappers. It holds no per-mapping state, so a
 * single shared instance is safe: the DTOs are immutable records and cannot form cycles.
 */
@Component
public class CycleAvoidingMappingContext {

    private final PlanMapper planMapper;
    private final SubscriptionContractMapper subscriptionContractMapper;
    private final PlanRateMapper planRateMapper;

    public CycleAvoidingMappingContext(@Lazy PlanMapper planMapper,
                                       @Lazy SubscriptionContractMapper subscriptionContractMapper,
                                       @Lazy PlanRateMapper planRateMapper) {
        this.planMapper = planMapper;
        this.subscriptionContractMapper = subscriptionContractMapper;
        this.planRateMapper = planRateMapper;
    }

    public PlanMapper getPlanMapper() {
        return planMapper;
    }

    public SubscriptionContractMapper getSubscriptionContractMapper() {
        return subscriptionContractMapper;
    }

    public PlanRateMapper getPlanRateMapper() {
        return planRateMapper;
    }
}
