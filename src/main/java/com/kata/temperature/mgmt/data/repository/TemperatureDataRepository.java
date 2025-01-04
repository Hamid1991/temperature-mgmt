package com.kata.temperature.mgmt.data.repository;

import com.kata.temperature.mgmt.data.entity.TemperatureData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureDataRepository extends CrudRepository<TemperatureData, Long>, TemperatureDataCustomRepository {}
