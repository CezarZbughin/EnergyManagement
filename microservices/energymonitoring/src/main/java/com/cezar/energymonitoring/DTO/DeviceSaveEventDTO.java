package com.cezar.energymonitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeviceSaveEventDTO {
    private String crud;
    private Long id;
    private String description;
    private String address;
    private Float maxHourlyConsumption;
    private Long ownerId;
}
