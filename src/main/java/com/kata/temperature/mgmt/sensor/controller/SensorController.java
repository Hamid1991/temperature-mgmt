package com.kata.temperature.mgmt.sensor.controller;

import com.kata.temperature.mgmt.sensor.entity.Sensor;
import com.kata.temperature.mgmt.sensor.service.SensorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("")
    public List<Sensor> getAllSensors() {
        return sensorService.findAllSensors();
    }

    @GetMapping("/{id}")
    public Sensor getSensorById(@PathVariable Long id) {
        return sensorService.findSensorById(id);
    }

    @PostMapping("")
    public Sensor createSensor(@RequestBody Sensor sensor) {
        return sensorService.createSensor(sensor);
    }

    @PutMapping("/{id}/assign-captor/{captorId}")
    public Sensor assignCaptorToSensor(@PathVariable Long id, @PathVariable Long captorId) {
        return sensorService.assignCaptorToSensor(id, captorId);
    }

    @PutMapping("/{id}/sync-state")
    public Sensor syncSensorState(@PathVariable Long id) {
        return sensorService.syncSensorState(id);
    }
}
