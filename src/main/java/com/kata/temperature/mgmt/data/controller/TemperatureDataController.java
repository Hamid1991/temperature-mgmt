package com.kata.temperature.mgmt.data.controller;

import com.kata.temperature.mgmt.data.entity.TemperatureData;
import com.kata.temperature.mgmt.data.service.TemperatureDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/temperature")
public class TemperatureDataController {

    private final TemperatureDataService temperatureDataService;

    public TemperatureDataController(TemperatureDataService temperatureDataService) {
        this.temperatureDataService = temperatureDataService;
    }

    @GetMapping("/{id}")
    public TemperatureData findTemperatureDataById(@PathVariable Long id) {
        return temperatureDataService.findTemperatureDataById(id);
    }

    @PostMapping("")
    public TemperatureData createTemperatureData(@RequestBody TemperatureData temperatureData) {
        return temperatureDataService.createTemperatureData(temperatureData);
    }

    @GetMapping("")
    public List<TemperatureData> findAllTemperaturesData() {
        return temperatureDataService.findAllTemperaturesData();
    }

    @GetMapping("/captor/{captorId}")
    public List<TemperatureData> findTemperatureDataByCaptorId(@PathVariable Long captorId) {
        return temperatureDataService.findAllTemperaturesDataByCaptorId(captorId);
    }

    @GetMapping("/sensor/{sensorId}")
    public List<TemperatureData> findTemperatureDataBySensorId(@PathVariable Long sensorId) {
        return temperatureDataService.findAllTemperaturesDataBySensorId(sensorId);
    }
}

