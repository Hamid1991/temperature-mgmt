package com.kata.temperature.mgmt.data.repository;

import com.kata.temperature.mgmt.data.entity.TemperatureData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemperatureDataCustomRepository {

    List<TemperatureData> findAllTemperaturesDataByCaptorId(Long captorId);
}