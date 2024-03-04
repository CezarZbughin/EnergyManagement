package com.cezar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ConsumptionEventDTO {
    private long timestamp;
    private Long deviceId;
    private float measurementValue;
}