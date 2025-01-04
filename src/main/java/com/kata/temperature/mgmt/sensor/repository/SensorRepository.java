package com.kata.temperature.mgmt.sensor.repository;

import com.kata.temperature.mgmt.sensor.entity.Sensor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends CrudRepository<Sensor, Long> {}
