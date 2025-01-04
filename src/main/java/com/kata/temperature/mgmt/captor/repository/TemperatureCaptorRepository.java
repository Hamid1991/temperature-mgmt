package com.kata.temperature.mgmt.captor.repository;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureCaptorRepository extends CrudRepository <TemperatureCaptor, Long>, TemperatureCaptorCustomRepository{}
