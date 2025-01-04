package com.kata.temperature.mgmt.sensor.entity;

import com.kata.temperature.mgmt.sensor.State;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Builder
@Table(name = "state_limits")
@NoArgsConstructor
@AllArgsConstructor
public class StateLimits {

    @Id
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "min", nullable = false)
    private BigDecimal min;

    @Column(name = "max")
    private BigDecimal max;
}
