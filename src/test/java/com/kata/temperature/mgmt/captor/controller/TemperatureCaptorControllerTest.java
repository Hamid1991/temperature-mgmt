package com.kata.temperature.mgmt.captor.controller;

import com.kata.temperature.mgmt.captor.repository.TemperatureCaptorRepository;
import com.kata.temperature.mgmt.captor.service.TemperatureCaptorService;
import com.kata.temperature.mgmt.sensor.State;
import com.kata.temperature.mgmt.sensor.entity.Sensor;
import com.kata.temperature.mgmt.sensor.repository.SensorRepository;
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

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TemperatureCaptorControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine")
    );

    @LocalServerPort
    private int port;

    @Autowired
    private TemperatureCaptorService temperatureCaptorService;

    @Autowired
    private TemperatureCaptorRepository temperatureCaptorRepository;
    
    @Autowired
    private SensorRepository sensorRepository;
    
    
    @Before
    public void setUp() {
        RestAssured.port = port;
        temperatureCaptorRepository.deleteAll();
    }

    @Test
    public void shouldCreateACaptorWithoutSensor() {
        String temperatureCaptorEmptyBody = """
                  {}
                """;
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(temperatureCaptorEmptyBody)
                .port(port)
                .when()
                .post("/temperature-captor")
                .then()
                .statusCode(200)
                .body("sensorId", nullValue());
    }

    @Test
    public void shouldCreateACaptorWithSensor() {

        Sensor createdSensor = createSensor();
        Long createdSensorId = createdSensor.getId();

        String temperatureCaptorWithSensorId =
                format("""
                  {
                    "sensorId": %d
                  }
                """, createdSensorId);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .body(temperatureCaptorWithSensorId)
                .when()
                .post("/temperature-captor")
                .then()
                .statusCode(200)
                .body("sensorId", equalTo(createdSensorId.intValue()));
    }



    private Sensor createSensor() {
        Sensor sensorToBeCreated = Sensor.builder().state(State.HOT).build();
        return sensorRepository.save(sensorToBeCreated);
    }

    @Test
    public void shouldGetTwoCaptors(){
        shouldCreateACaptorWithoutSensor();
        shouldCreateACaptorWithSensor();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .get("/temperature-captor")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

}