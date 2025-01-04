package com.kata.temperature.mgmt.sensor.controller;

import com.kata.temperature.mgmt.sensor.State;
import com.kata.temperature.mgmt.sensor.entity.StateLimits;
import com.kata.temperature.mgmt.sensor.service.StateLimitsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statelimits")
public class StateLimitsController {

    private final StateLimitsService stateLimitsService;

    public StateLimitsController(StateLimitsService stateLimitsService) {
        this.stateLimitsService = stateLimitsService;
    }

    @GetMapping("")
    public List<StateLimits> getAllStateLimits() {
        return  stateLimitsService.findAllStateLimits();
    }

    @GetMapping("/{state}")
    public StateLimits getStateLimitsByState(@PathVariable String state) {
        State searchedState = State.valueOf(state);
        return stateLimitsService.findStateLimitsByState(searchedState);
    }

    @PostMapping("")
    public StateLimits createStateLimits(@RequestBody StateLimits stateLimits) {
        return stateLimitsService.createStateLimits(stateLimits);
    }

    @PutMapping("/{state}")
    public StateLimits updateStateLimits(@PathVariable String state, @RequestBody StateLimits updatedStateLimits) {
        State stateToBeUpdated = State.valueOf(state);
        return stateLimitsService.updateStateLimits(stateToBeUpdated, updatedStateLimits);
    }

    @DeleteMapping("/{state}")
    public void deleteStateLimits(@PathVariable String state) {
        State stateToBeDeleted = State.valueOf(state);
        stateLimitsService.delete(stateToBeDeleted);
    }

}
