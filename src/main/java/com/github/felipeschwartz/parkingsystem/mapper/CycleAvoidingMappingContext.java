package com.github.felipeschwartz.parkingsystem.mapper;

import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class CycleAvoidingMappingContext {

    private Map<Object, Object> knownInstances = new IdentityHashMap<>();

    private PlanMapper planMapper;
    private SubscriptionContractMapper subscriptionContractMapper;
    private PlanRateMapper planRateMapper;

    public CycleAvoidingMappingContext(PlanMapper planMapper, SubscriptionContractMapper subscriptionContractMapper, PlanRateMapper planRateMapper) {
        this.planMapper = planMapper;
        this.subscriptionContractMapper = subscriptionContractMapper;
        this.planRateMapper = planRateMapper;
    }

    public CycleAvoidingMappingContext() {

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

    @BeforeMapping
    public <T> T getMappedInstance(Object source, @TargetType Class<T> targetType) {
        return (T) knownInstances.get(source);
    }

    @BeforeMapping
    public void storeMappedInstance(Object source, @MappingTarget Object target) {
        knownInstances.put(source, target);
    }

    public <T> T getMappedInstance(Object source, Class<T> targetType, Supplier<T> mappingFunction) {
        if (source == null) {
            return null;
        }
        T target = (T) knownInstances.get(source);
        if (target != null) {
            return target;
        }
        target = mappingFunction.get();
        knownInstances.put(source, target);
        return target;
    }


}