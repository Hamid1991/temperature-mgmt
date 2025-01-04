package com.kata.temperature.mgmt.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Degree {
    @JsonProperty("°C")
    Celsius("°C"),
    @JsonProperty("°F")
    Fahrenheit("°F"),
    @JsonProperty("°K")
    Kelvin("°K");

    public final String value;

    Degree(String value) {
        this.value = value;
    }
}