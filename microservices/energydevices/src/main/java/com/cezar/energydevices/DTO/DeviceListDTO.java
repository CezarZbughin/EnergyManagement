package com.cezar.energydevices.DTO;

import com.cezar.energydevices.model.Device;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DeviceListDTO {
    private List<Device> devices;
}
