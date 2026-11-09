package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelAssemblersTest {

    private static List<String> rels(EntityModel<?> model) {
        return model.getLinks().stream().map(l -> l.getRel().value()).toList();
    }

    private static Link link(EntityModel<?> model, String rel) {
        return model.getRequiredLink(rel);
    }

    @Test
    void crudAssemblersShouldExposeTheFiveStandardLinks() {
        List<EntityModel<?>> models = List.of(
                new HourlyRateModelAssembler().toModel(TestDtos.hourlyRate(1L)),
                new ParkingLotModelAssembler().toModel(TestDtos.parkingLot(1L)),
                new ParkingSpaceModelAssembler().toModel(TestDtos.parkingSpace(1L)),
                new PlanRateModelAssembler().toModel(TestDtos.planRate(1L)),
                new ReservationModelAssembler().toModel(TestDtos.reservation(1L)),
                new VehicleModelAssembler().toModel(TestDtos.vehicle(1L, "ABC1234")),
                new PlanModelAssembler().toModel(TestDtos.plan(1L)));

        for (EntityModel<?> model : models) {
            assertEquals(List.of("self", "findAll", "create", "update", "delete"), rels(model), model.getContent().toString());
        }
    }

    @Test
    void selfLinkShouldPointAtTheResourceRoute() {
        assertTrue(link(new HourlyRateModelAssembler().toModel(TestDtos.hourlyRate(7L)), "self").getHref()
                .endsWith("/api/hourly_rate/v1/7"));
        assertTrue(link(new VehicleModelAssembler().toModel(TestDtos.vehicle(7L, "A")), "self").getHref()
                .endsWith("/api/vehicle/v1/id/7"));
        assertTrue(link(new PlanModelAssembler().toModel(TestDtos.plan(7L)), "self").getHref()
                .endsWith("/api/plan/v1/7"));
    }

    @Test
    void methodsShouldMatchTheRel() {
        EntityModel<?> model = new HourlyRateModelAssembler().toModel(TestDtos.hourlyRate(1L));

        assertEquals("GET", link(model, "self").getType());
        assertEquals("POST", link(model, "create").getType());
        assertEquals("PUT", link(model, "update").getType());
        assertEquals("DELETE", link(model, "delete").getType());
    }

    @Test
    void paymentShouldNotExposeDelete() {
        EntityModel<?> model = new PaymentModelAssembler().toModel(TestDtos.payment(1L));

        assertEquals(List.of("self", "findAll", "create", "update"), rels(model));
    }

    @Test
    void subscriptionContractShouldExposeRenew() {
        EntityModel<?> model = new SubscriptionContractModelAssembler().toModel(TestDtos.subscriptionContract(1L));

        assertEquals(List.of("self", "findAll", "create", "update", "renew", "delete"), rels(model));
    }

    @Test
    void renewLinkShouldPointAtTheRenewRoute() {
        EntityModel<?> model = new SubscriptionContractModelAssembler().toModel(TestDtos.subscriptionContract(7L));

        assertTrue(link(model, "renew").getHref().endsWith("/api/contracts/v1/renew/7"));
        assertEquals("PUT", link(model, "renew").getType());
    }

    @Test
    void parkingSessionShouldExposeOpenAndClose() {
        EntityModel<?> model = new ParkingSessionModelAssembler().toModel(TestDtos.parkingSession(1L));

        assertEquals(List.of("self", "findAll", "create", "close"), rels(model));
    }

    @Test
    void closeLinkShouldUseTheVerbOfTheCloseRoute() {
        EntityModel<?> model = new ParkingSessionModelAssembler().toModel(TestDtos.parkingSession(7L));

        assertEquals("POST", link(model, "close").getType());
        assertTrue(link(model, "close").getHref().endsWith("/api/parking_sessions/v1/7/close"));
    }

    @Test
    void userAssemblerShouldAddFindByCpfOnlyForIndividualsWithACpf() {
        UserModelAssembler assembler = new UserModelAssembler();

        EntityModel<?> withCpf = assembler.toModel(TestDtos.userIndividual(1L, "123"));
        EntityModel<?> withoutCpf = assembler.toModel(TestDtos.userIndividual(1L, null));

        assertTrue(withCpf.hasLink("findByCpf"));
        assertTrue(link(withCpf, "findByCpf").getHref().endsWith("/api/user/v1/cpf/123"));
        assertFalse(withoutCpf.hasLink("findByCpf"));
    }

    @Test
    void userAssemblerShouldAddFindByCnpjOnlyForEntitiesWithACnpj() {
        UserModelAssembler assembler = new UserModelAssembler();

        EntityModel<?> withCnpj = assembler.toModel(TestDtos.userEntity(1L, "999"));
        EntityModel<?> withoutCnpj = assembler.toModel(TestDtos.userEntity(1L, null));

        assertTrue(link(withCnpj, "findByCnpj").getHref().endsWith("/api/user/v1/cnpj/999"));
        assertFalse(withoutCnpj.hasLink("findByCnpj"));
        assertEquals(List.of("self", "findAllUsers", "createUser", "updateUser", "deleteUser", "findByCnpj"), rels(withCnpj));
    }
}
