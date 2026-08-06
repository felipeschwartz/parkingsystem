package com.github.felipeschwartz.parkingsystem.integrationtests.swagger;

import com.github.felipeschwartz.parkingsystem.config.Testconfigs;
import com.github.felipeschwartz.parkingsystem.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldReturnSwagger() {
        var content = given()
                .basePath("/swagger-ui/index.html")
                    .port(Testconfigs.SERVER_PORT)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();
        assertTrue(content.contains("Swagger UI"));
    }
}
