package com.kata.temperature.mgmt.sensor.repository;

import com.kata.temperature.mgmt.sensor.State;
import com.kata.temperature.mgmt.sensor.entity.StateLimits;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateLimitsRepository extends CrudRepository<StateLimits, State>, StateLimitsCustomRepository {}
