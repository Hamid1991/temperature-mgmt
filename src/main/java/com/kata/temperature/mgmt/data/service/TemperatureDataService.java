package com.kata.temperature.mgmt.data.service;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import com.kata.temperature.mgmt.captor.service.TemperatureCaptorService;
import com.kata.temperature.mgmt.data.entity.TemperatureData;
import com.kata.temperature.mgmt.data.repository.TemperatureDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class TemperatureDataService {

    private final TemperatureDataRepository temperatureDataRepository;

    private final TemperatureCaptorService temperatureCaptorService;

    public TemperatureDataService(TemperatureDataRepository temperatureDataRepository, TemperatureCaptorService temperatureCaptorService) {
        this.temperatureDataRepository = temperatureDataRepository;
        this.temperatureCaptorService = temperatureCaptorService;
    }

    public TemperatureData findTemperatureDataById(Long id) {
        return temperatureDataRepository.findById(id).orElse(null);
    }

    public List<TemperatureData> findAllTemperaturesData() {
        Iterable<TemperatureData> allTemperaturesData = temperatureDataRepository.findAll();
        return StreamSupport.stream(allTemperaturesData.spliterator(), false).toList();
    }

    public TemperatureData createTemperatureData(TemperatureData temperatureData) {
        return temperatureDataRepository.save(temperatureData);
    }

    public TemperatureData findLastTemperatureDataByCaptorId(Long captorId) {
        List<TemperatureData> results = findAllTemperaturesDataByCaptorId(captorId);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<TemperatureData> findAllTemperaturesDataByCaptorId(Long captorId) {
        return temperatureDataRepository.findAllTemperaturesDataByCaptorId(captorId);
    }

    public List<TemperatureData> findAllTemperaturesDataBySensorId(Long sensorId) {
        TemperatureCaptor captorBySensorId = temperatureCaptorService.findCaptorBySensorId(sensorId);
        Long captorId = captorBySensorId.getId();
        return temperatureDataRepository.findAllTemperaturesDataByCaptorId(captorId);
    }
}
