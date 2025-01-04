package com.kata.temperature.mgmt.captor.entity;

import com.kata.temperature.mgmt.data.entity.TemperatureData;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "temperature_captor")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TemperatureCaptor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sensor_id")
    private Long sensorId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "captorId", orphanRemoval = true)
    private List<TemperatureData> temperatureData;

}
