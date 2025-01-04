package com.kata.temperature.mgmt.captor.controller;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import com.kata.temperature.mgmt.captor.service.TemperatureCaptorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/temperature-captor")
public class TemperatureCaptorController {

    private final TemperatureCaptorService temperatureCaptorService;

    public TemperatureCaptorController(TemperatureCaptorService temperatureCaptorService) {
        this.temperatureCaptorService = temperatureCaptorService;
    }

    @GetMapping("/{id}")
    public TemperatureCaptor getTemperatureCaptorById(@PathVariable Long id) {
        return temperatureCaptorService.findCaptorById(id);
    }

    @GetMapping
    public List<TemperatureCaptor> getAllTemperatureCaptors() {
        return temperatureCaptorService.findAllCaptors();
    }

    @PostMapping
    public TemperatureCaptor createTemperatureCaptor(@RequestBody TemperatureCaptor temperatureCaptor) {
        return temperatureCaptorService.createCaptor(temperatureCaptor);
    }
}
