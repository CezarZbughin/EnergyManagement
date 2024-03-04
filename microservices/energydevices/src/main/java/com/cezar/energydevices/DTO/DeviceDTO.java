package com.cezar.energydevices.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeviceDTO {
    private String description;
    private String address;
    private Float maxHourlyConsumption;
    private Long ownerId;
}
