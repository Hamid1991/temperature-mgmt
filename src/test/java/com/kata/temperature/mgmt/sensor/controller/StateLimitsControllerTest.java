package com.kata.temperature.mgmt.sensor.controller;

import com.kata.temperature.mgmt.sensor.repository.StateLimitsRepository;
import com.kata.temperature.mgmt.sensor.service.StateLimitsService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class StateLimitsControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine")
    );

    @LocalServerPort
    private int port;

    @Autowired
    private StateLimitsService stateLimitsService;

    @Autowired
    private StateLimitsRepository stateLimitsRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        stateLimitsRepository.deleteAll();
    }

    @Test
    public void shouldCreateAUniqueCOLDStateLimits() {
        String coldStateLimitsBody = """
                  {
                    "state": "COLD",
                    "max": 20.0,
                    "min": -100.0
                  }
                """;
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(coldStateLimitsBody)
                .port(port)
                .when()
                .post("/statelimits")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldGetASingleStateLimits() {

        shouldCreateAUniqueCOLDStateLimits();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .get("/statelimits")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    public void shouldGetAUniqueStateLimitsForCOLD() {

        shouldCreateAUniqueCOLDStateLimits();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .get("/statelimits/COLD")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldUpdateLimitsForCOLDState() {

        shouldCreateAUniqueCOLDStateLimits();

        String coldStateUpdatedLimits = """
                  {
                    "state": "COLD",
                    "max": 19.0,
                    "min": -190.0
                  }
                """;

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .body(coldStateUpdatedLimits)
                .when()
                .put("/statelimits/COLD")
                .then()
                .statusCode(200)
                .body("state", equalTo("COLD"))
                .body("max", equalTo(19.0F))
                .body("min", equalTo(-190.0F));
    }

}