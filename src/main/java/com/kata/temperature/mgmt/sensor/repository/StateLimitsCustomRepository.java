package com.kata.temperature.mgmt.sensor.repository;

import com.kata.temperature.mgmt.sensor.entity.StateLimits;

import java.math.BigDecimal;

public interface StateLimitsCustomRepository {

    StateLimits findStateLimitsByTemperatureMeasure(BigDecimal temperatureMeasure);
}