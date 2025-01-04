package com.kata.temperature.mgmt.captor.service;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import com.kata.temperature.mgmt.captor.repository.TemperatureCaptorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class TemperatureCaptorService {

    private final TemperatureCaptorRepository temperatureCaptorRepository;

    public TemperatureCaptorService(TemperatureCaptorRepository temperatureCaptorRepository) {
        this.temperatureCaptorRepository = temperatureCaptorRepository;
    }

    public List<TemperatureCaptor> findAllCaptors() {
        Iterable<TemperatureCaptor> allCaptors = temperatureCaptorRepository.findAll();
        return StreamSupport.stream(allCaptors.spliterator(), false).toList();
    }

    public TemperatureCaptor findCaptorById(Long id) {
        return temperatureCaptorRepository.findById(id)
                .orElse(null);
    }

    public TemperatureCaptor findCaptorBySensorId(Long sensorId) {
        return temperatureCaptorRepository.findTemperatureCaptorBySensorId(sensorId);
    }

    public TemperatureCaptor createCaptor(TemperatureCaptor captor) {
        return temperatureCaptorRepository.save(captor);
    }

    public void updateCaptor(Long captorId, TemperatureCaptor captor) {
        TemperatureCaptor temperatureCaptorToBeUpdated = temperatureCaptorRepository.findById(captorId).get();
        temperatureCaptorToBeUpdated.setSensorId(captor.getSensorId());
        temperatureCaptorToBeUpdated.setTemperatureData(captor.getTemperatureData());
        temperatureCaptorRepository.save(captor);
    }
}

