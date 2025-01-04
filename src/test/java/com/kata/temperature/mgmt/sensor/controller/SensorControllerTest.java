package com.kata.temperature.mgmt.sensor.controller;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import com.kata.temperature.mgmt.captor.repository.TemperatureCaptorRepository;
import com.kata.temperature.mgmt.data.Degree;
import com.kata.temperature.mgmt.data.entity.TemperatureData;
import com.kata.temperature.mgmt.data.repository.TemperatureDataRepository;
import com.kata.temperature.mgmt.sensor.State;
import com.kata.temperature.mgmt.sensor.entity.StateLimits;
import com.kata.temperature.mgmt.sensor.repository.SensorRepository;
import com.kata.temperature.mgmt.sensor.repository.StateLimitsRepository;
import com.kata.temperature.mgmt.sensor.service.SensorService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class SensorControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine")
    );

    @LocalServerPort
    private int port;

    @Autowired
    private SensorService stateLimitsService;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private TemperatureCaptorRepository temperatureCaptorRepository;

    @Autowired
    private TemperatureDataRepository temperatureDataRepository;

    @Autowired
    private StateLimitsRepository stateLimitsRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        sensorRepository.deleteAll();
        temperatureDataRepository.deleteAll();
        temperatureCaptorRepository.deleteAll();
        stateLimitsRepository.deleteAll();
    }

    @Test
    public void shouldCreateASensorWithoutCaptor() {
        String sensorBody = """
                  {
                    "state": "COLD"
                  }
                """;

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(sensorBody)
                .port(port)
                .when()
                .post("/sensor")
                .then()
                .statusCode(200)
                .body("state", equalTo("COLD"));
    }

    @Test
    public void shouldGetASingleSensor() {

        shouldCreateASensorWithoutCaptor();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .get("/sensor")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    public void shouldAssignCaptorToSensor() {
        shouldCreateASensorWithoutCaptor();
        TemperatureCaptor createdTemperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = createdTemperatureCaptor.getId();
        Long createdSenorId = getCreatedSensorId();

        invokeAndValidateAssignCaptorToSensor(createdSenorId, createdTemperatureCaptorId);
    }

    private Long getCreatedSensorId() {
        return sensorRepository.findAll().iterator().next().getId();
    }

    private void invokeAndValidateAssignCaptorToSensor(Long createdSensorId,Long createdTemperatureCaptorId) {

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .put("sensor/{id}/assign-captor/{captorId}", createdSensorId, createdTemperatureCaptorId)
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldSyncSensorStateWithHOT_whenTemperatureMeasureIs50() {
        shouldCreateASensorWithoutCaptor();
        TemperatureCaptor createdTemperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = createdTemperatureCaptor.getId();
        updateTemperatureCaptorWithTemperatureData(50.0, createdTemperatureCaptor);
        Long createdSensorId = getCreatedSensorId();
        invokeAndValidateAssignCaptorToSensor(createdSensorId, createdTemperatureCaptorId);
        createStatesLimits();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .put("/sensor/{id}/sync-state", createdSensorId)
                .then()
                .statusCode(200)
                .body("state", equalTo(State.HOT.name()));
    }


    @Test
    public void shouldSyncSensorStateWithHOT_whenTemperatureMeasureIs40() {
        shouldCreateASensorWithoutCaptor();
        TemperatureCaptor createdTemperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = createdTemperatureCaptor.getId();
        updateTemperatureCaptorWithTemperatureData( 40.0, createdTemperatureCaptor);
        Long createdSensorId = getCreatedSensorId();
        invokeAndValidateAssignCaptorToSensor(createdSensorId, createdTemperatureCaptorId);
        createStatesLimits();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .put("/sensor/{id}/sync-state", createdSensorId)
                .then()
                .statusCode(200)
                .body("state", equalTo(State.HOT.name()));
    }

    @Test
    public void shouldSyncSensorStateWithWARM_whenTemperatureMeasureIs30() {
        shouldCreateASensorWithoutCaptor();
        TemperatureCaptor createdTemperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = createdTemperatureCaptor.getId();
        updateTemperatureCaptorWithTemperatureData( 30.0, createdTemperatureCaptor);
        Long createdSensorId = getCreatedSensorId();
        invokeAndValidateAssignCaptorToSensor(createdSensorId, createdTemperatureCaptorId);
        createStatesLimits();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .put("/sensor/{id}/sync-state", createdSensorId)
                .then()
                .statusCode(200)
                .body("state", equalTo(State.WARM.name()));
    }

    @Test
    public void shouldSyncSensorStateWithWARM_whenTemperatureMeasureIs22() {
        shouldCreateASensorWithoutCaptor();
        TemperatureCaptor createdTemperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = createdTemperatureCaptor.getId();
        updateTemperatureCaptorWithTemperatureData( 22.0, createdTemperatureCaptor);
        Long createdSensorId = getCreatedSensorId();
        invokeAndValidateAssignCaptorToSensor(createdSensorId, createdTemperatureCaptorId);
        createStatesLimits();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .put("/sensor/{id}/sync-state", createdSensorId)
                .then()
                .statusCode(200)
                .body("state", equalTo(State.WARM.name()));
    }

    @Test
    public void shouldSyncSensorStateWithCOLD_whenTemperatureMeasureIs10() {
        shouldCreateASensorWithoutCaptor();
        TemperatureCaptor createdTemperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = createdTemperatureCaptor.getId();
        updateTemperatureCaptorWithTemperatureData( 10.0, createdTemperatureCaptor);
        Long createdSensorId = getCreatedSensorId();
        invokeAndValidateAssignCaptorToSensor(createdSensorId, createdTemperatureCaptorId);
        createStatesLimits();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .put("/sensor/{id}/sync-state", createdSensorId)
                .then()
                .statusCode(200)
                .body("state", equalTo(State.COLD.name()));
    }

    private void updateTemperatureCaptorWithTemperatureData(double val, TemperatureCaptor createdTemperatureCaptor) {
        Long createdTemperatureCaptorId = createdTemperatureCaptor.getId();
        TemperatureData createdTemperatureData = createTemperatureData(createdTemperatureCaptorId, BigDecimal.valueOf(val));
        createdTemperatureCaptor.setTemperatureData(List.of(createdTemperatureData));
        temperatureCaptorRepository.save(createdTemperatureCaptor);
    }

    private void createStatesLimits() {
        List<StateLimits> allStateLimits = new ArrayList<>();
        StateLimits hotStateLimits = StateLimits.builder()
                .state(State.HOT)
                .min(BigDecimal.valueOf(40.0))
                .max(BigDecimal.valueOf(100.0))
                .build();
        allStateLimits.add(hotStateLimits);
        StateLimits warmStateLimits = StateLimits.builder()
                .state(State.WARM)
                .min(BigDecimal.valueOf(22.0))
                .max(BigDecimal.valueOf(40.0))
                .build();
        allStateLimits.add(warmStateLimits);
        StateLimits coldStateLimits = StateLimits.builder()
                .state(State.COLD)
                .min(BigDecimal.valueOf(-100.0))
                .max(BigDecimal.valueOf(22.0))
                .build();
        allStateLimits.add(coldStateLimits);

        stateLimitsRepository.saveAll(allStateLimits);
    }

    private TemperatureData createTemperatureData(Long captorId, BigDecimal temperatureMeasure) {
        TemperatureData temperatureData = TemperatureData.builder()
                .captorId(captorId)
                .degree(Degree.Celsius)
                .measure(temperatureMeasure)
                .timestamp(LocalDateTime.now())
                .build();

        return temperatureDataRepository.save(temperatureData);
    }


    private TemperatureCaptor createTemperatureCaptor() {
        TemperatureCaptor temperatureCaptorToBeCreated = TemperatureCaptor
                .builder()
                .build();

        return temperatureCaptorRepository.save(temperatureCaptorToBeCreated);
    }
}