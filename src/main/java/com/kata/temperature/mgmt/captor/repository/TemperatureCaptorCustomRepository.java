package com.kata.temperature.mgmt.captor.repository;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureCaptorCustomRepository {

    TemperatureCaptor findTemperatureCaptorBySensorId(Long sensorId);
}