package com.github.felipeschwartz.parkingsystem.config;

import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import com.github.felipeschwartz.parkingsystem.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@WebMvcTest
@Import({SecurityConfig.class, JwtFilter.class})
@MockitoBean(types = {
        HourlyRateService.class, ParkingLotService.class, ParkingSessionService.class,
        ParkingSpaceService.class, PaymentService.class, PlanRateService.class, PlanService.class,
        ReservationService.class, SubscriptionContractService.class, UserService.class,
        VehicleService.class, AuthService.class, JwtService.class, UserDetailsService.class
})
class SecurityConfigTest {

    private static final List<String> ALL_ROLES = List.of("ADMIN", "USER", "PARKING", "PARKING_MANAGER");

    @Autowired
    private MockMvc mvc;

    static Stream<Arguments> rules() {
        List<String> all = ALL_ROLES;
        List<String> admin = List.of("ADMIN");
        List<String> adminManager = List.of("ADMIN", "PARKING_MANAGER");
        List<String> adminUser = List.of("ADMIN", "USER");
        List<String> adminManagerUser = List.of("ADMIN", "PARKING_MANAGER", "USER");
        List<String> operators = List.of("ADMIN", "PARKING", "PARKING_MANAGER");

        return Stream.of(
                Arguments.of(HttpMethod.GET, "/api/user/v1", adminManager),
                Arguments.of(HttpMethod.POST, "/api/user/v1", admin),
                Arguments.of(HttpMethod.PUT, "/api/user/v1/1", all),
                Arguments.of(HttpMethod.DELETE, "/api/user/v1/id/1", admin),
                Arguments.of(HttpMethod.GET, "/api/vehicle/v1", all),
                Arguments.of(HttpMethod.GET, "/api/vehicle/v1/licence_plate/ABC1234", all),
                Arguments.of(HttpMethod.POST, "/api/vehicle/v1", adminUser),
                Arguments.of(HttpMethod.PUT, "/api/vehicle/v1/1", adminUser),
                Arguments.of(HttpMethod.DELETE, "/api/vehicle/v1/1", admin),
                Arguments.of(HttpMethod.GET, "/api/parking_lot/v1", adminManager),
                Arguments.of(HttpMethod.DELETE, "/api/parking_lot/v1/id/1", adminManager),
                Arguments.of(HttpMethod.GET, "/api/parking_space/v1", adminManager),
                Arguments.of(HttpMethod.GET, "/api/hourly_rate/v1", adminManager),
                Arguments.of(HttpMethod.GET, "/api/plan/v1", adminManager),
                Arguments.of(HttpMethod.PUT, "/api/plan/v1/activate/1", adminManager),
                Arguments.of(HttpMethod.GET, "/api/plan_rate/v1", adminManager),
                Arguments.of(HttpMethod.GET, "/api/contracts/v1", adminManagerUser),
                Arguments.of(HttpMethod.GET, "/api/reservation/v1", adminManagerUser),
                Arguments.of(HttpMethod.GET, "/api/payment/v1", adminManager),
                Arguments.of(HttpMethod.POST, "/api/parking_sessions/v1/open", operators),
                Arguments.of(HttpMethod.POST, "/api/parking_sessions/v1/1/close", operators),
                Arguments.of(HttpMethod.GET, "/api/parking_sessions/v1", all),
                Arguments.of(HttpMethod.GET, "/api/parking_sessions/v1/open/space/1", all)
        );
    }

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("rules")
    void shouldEnforceRolesOnRealControllerPaths(HttpMethod method, String path, List<String> allowedRoles) {
        for (String role : ALL_ROLES) {
            boolean denied = isDenied(method, path, user("u").roles(role));
            assertEquals(!allowedRoles.contains(role), denied,
                    "role " + role + " on " + method + " " + path + (denied ? " was denied" : " was allowed"));
        }
        assertEquals(true, isDenied(method, path, null), "anonymous must be denied on " + method + " " + path);
    }

    @Test
    void shouldAllowUserToReachOwnProfileLookups() {
        UserDetails self = principalWithId(1L);

        assertFalse(isDenied(HttpMethod.GET, "/api/user/v1/id/1", user(self)));
    }

    @Test
    void shouldKeepPublicEndpointsOpen() {
        assertFalse(isDenied(HttpMethod.POST, "/auth/login", null));
        assertFalse(isDenied(HttpMethod.GET, "/api/test/v1", null));
    }

    private UserDetails principalWithId(Long id) {
        UserIndividual individual = new UserIndividual();
        individual.setId(id);
        individual.setEmail("self@teste.com");
        individual.setPassword("encoded");
        individual.setCpf("11111111111");
        individual.setRoles(Set.of("ROLE_USER"));
        return new CustomUserDetails(individual);
    }

    private boolean isDenied(HttpMethod method, String path, RequestPostProcessor auth) {
        try {
            var builder = request(method, path);
            if (auth != null) {
                builder = builder.with(auth);
            }
            int status = mvc.perform(builder).andReturn().getResponse().getStatus();
            return status == 401 || status == 403;
        } catch (Exception e) {
            // A exceção vem do controller/serviço mockado, ou seja, a requisição passou pela URL; só AccessDenied conta como bloqueio.
            for (Throwable t = e; t != null; t = t.getCause()) {
                if (t instanceof AccessDeniedException) {
                    return true;
                }
            }
            return false;
        }
    }
}
