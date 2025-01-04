package com.kata.temperature.mgmt.sensor.service;

import com.kata.temperature.mgmt.sensor.State;
import com.kata.temperature.mgmt.sensor.entity.StateLimits;
import com.kata.temperature.mgmt.sensor.repository.StateLimitsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class StateLimitsService {

    private final StateLimitsRepository stateLimitsRepository;

    public StateLimitsService(StateLimitsRepository stateLimitsRepository) {
        this.stateLimitsRepository = stateLimitsRepository;
    }


    public List<StateLimits> findAllStateLimits() {
        Iterable<StateLimits> allStateLimits = stateLimitsRepository.findAll();
        return StreamSupport.stream(allStateLimits.spliterator(), false).toList();
    }


    public StateLimits findStateLimitsByState(State state) {
        return stateLimitsRepository.findById(state).orElse(null);
    }

    public StateLimits createStateLimits(StateLimits stateLimits) {
        return stateLimitsRepository.save(stateLimits);
    }

    public StateLimits updateStateLimits(State state, StateLimits updatedStateLimits) {
        StateLimits stateLimitsToBeUpdated = findStateLimitsByState(state);
        stateLimitsToBeUpdated.setMin(updatedStateLimits.getMin());
        stateLimitsToBeUpdated.setMax(updatedStateLimits.getMax());
        return stateLimitsRepository.save(stateLimitsToBeUpdated);
    }

    public void delete(State state) {
        stateLimitsRepository.deleteById(state);
    }

    public StateLimits findStateLimitsByTemperatureMeasure(BigDecimal temperatureMeasure) {
        return stateLimitsRepository.findStateLimitsByTemperatureMeasure(temperatureMeasure);
    }

}
