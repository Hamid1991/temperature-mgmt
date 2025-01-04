package com.kata.temperature.mgmt.data.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kata.temperature.mgmt.data.Degree;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "temperature_data")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TemperatureData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "captor_id")
    private Long captorId;

    @Column(name = "measure")
    private BigDecimal measure;

    @Column(name = "degree")
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @Column(name = "timestamp")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timestamp;
}