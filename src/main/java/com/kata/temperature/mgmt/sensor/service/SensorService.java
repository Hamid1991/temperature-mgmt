package com.kata.temperature.mgmt.sensor.service;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import com.kata.temperature.mgmt.captor.service.TemperatureCaptorService;
import com.kata.temperature.mgmt.data.entity.TemperatureData;
import com.kata.temperature.mgmt.data.service.TemperatureDataService;
import com.kata.temperature.mgmt.sensor.State;
import com.kata.temperature.mgmt.sensor.entity.Sensor;
import com.kata.temperature.mgmt.sensor.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    private final TemperatureCaptorService temperatureCaptorService;

    private final TemperatureDataService temperatureDataService;

    private final StateLimitsService stateLimitsService;

    public SensorService(SensorRepository sensorRepository, TemperatureCaptorService temperatureCaptorService, TemperatureDataService temperatureDataService, StateLimitsService stateLimitsService) {
        this.sensorRepository = sensorRepository;
        this.temperatureCaptorService = temperatureCaptorService;
        this.temperatureDataService = temperatureDataService;
        this.stateLimitsService = stateLimitsService;
    }

    public Sensor findSensorById(Long id) {
        return sensorRepository.findById(id).orElse(null);
    }

    public List<Sensor> findAllSensors() {
        Iterable<Sensor> allSensors = sensorRepository.findAll();
        return StreamSupport.stream(allSensors.spliterator(), false).toList();
    }

    public Sensor createSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public Sensor assignCaptorToSensor(Long sensorId, Long captorId) {
        Sensor sensor = findSensorById(sensorId);
        TemperatureCaptor captor = temperatureCaptorService.findCaptorById(captorId);
        captor.setSensorId(sensorId);
        temperatureCaptorService.updateCaptor(captorId, captor);
        sensor.setCaptor(captor);
        return sensorRepository.save(sensor);
    }

    public Sensor syncSensorState(Long sensorId) {
        Sensor sensorToSync = findSensorById(sensorId);
        Long captorId = sensorToSync.getCaptor().getId();
        TemperatureData lastTemperatureData = temperatureDataService.findLastTemperatureDataByCaptorId(captorId);
        BigDecimal temperatureMeasure = lastTemperatureData.getMeasure();
        State syncedState = stateLimitsService.findStateLimitsByTemperatureMeasure(temperatureMeasure).getState();
        sensorToSync.setState(syncedState);
        return sensorRepository.save(sensorToSync);
    }
    
}
