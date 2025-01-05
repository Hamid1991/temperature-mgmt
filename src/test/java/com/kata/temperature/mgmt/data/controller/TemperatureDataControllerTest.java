package com.kata.temperature.mgmt.data.controller;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import com.kata.temperature.mgmt.captor.repository.TemperatureCaptorRepository;
import com.kata.temperature.mgmt.data.service.TemperatureDataService;
import com.kata.temperature.mgmt.sensor.State;
import com.kata.temperature.mgmt.sensor.entity.Sensor;
import com.kata.temperature.mgmt.sensor.repository.SensorRepository;
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

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TemperatureDataControllerTest {


    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine")
    );

    @LocalServerPort
    private int port;

    @Autowired
    private TemperatureDataService temperatureDataService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private TemperatureCaptorRepository temperatureCaptorRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        sensorRepository.deleteAll();
        temperatureCaptorRepository.deleteAll();
    }


    @Test
    public void shouldCreateASingleDataTemperature(){

        TemperatureCaptor createdTemperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = createdTemperatureCaptor.getId();

        invokeAndValidateCreatedTemperatureData(createdTemperatureCaptorId);
    }

    private void invokeAndValidateCreatedTemperatureData(Long createdTemperatureCaptorId) {
        String temperatureDataBody = format("""
                   {
                     "captorId": %d,
                     "measure": 20.0,
                     "degree": "°C",
                     "timestamp": "06-12-2024 12:00:00"
                   }
                """, createdTemperatureCaptorId);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(temperatureDataBody)
                .port(port)
                .when()
                .post("/temperature")
                .then()
                .statusCode(200)
                .body("captorId", equalTo(createdTemperatureCaptorId.intValue()));
    }

    @Test
    public void shouldGetTwoTemperaturesData(){

        shouldCreateASingleDataTemperature();
        shouldCreateASingleDataTemperature();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .get("/temperature")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void shouldGetAllTemperatureDataBySensorId(){
        TemperatureCaptor temperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = temperatureCaptor.getId();
        Sensor createdSensor = createSensor();
        Long createdSensorId = createdSensor.getId();
        sensorService.assignCaptorToSensor(createdSensorId, createdTemperatureCaptorId);
        invokeAndValidateCreatedTemperatureData(createdTemperatureCaptorId);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .get("/temperature/sensor/{id}", createdSensorId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    public void shouldGetAllTemperatureDataByCaptorId(){
        TemperatureCaptor temperatureCaptor = createTemperatureCaptor();
        Long createdTemperatureCaptorId = temperatureCaptor.getId();
        invokeAndValidateCreatedTemperatureData(createdTemperatureCaptorId);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .port(port)
                .when()
                .get("/temperature/captor/{id}", createdTemperatureCaptorId)
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    private TemperatureCaptor createTemperatureCaptor() {
        TemperatureCaptor createdTemperatureCaptor = TemperatureCaptor.builder().build();
        return temperatureCaptorRepository.save(createdTemperatureCaptor);
    }

    private Sensor createSensor() {
        Sensor sensorToBeCreated = Sensor.builder()
                .state(State.HOT)
                .build();
        return sensorRepository.save(sensorToBeCreated);
    }
}