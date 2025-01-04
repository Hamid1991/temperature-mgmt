package com.kata.temperature.mgmt.sensor.entity;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import com.kata.temperature.mgmt.sensor.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "sensor")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "captor_id", referencedColumnName = "id")
    private TemperatureCaptor captor;
}
