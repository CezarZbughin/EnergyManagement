package com.cezar.energymonitoring.service;

import com.cezar.energymonitoring.DTO.DeviceSaveEventDTO;
import com.cezar.energymonitoring.model.Device;
import com.cezar.energymonitoring.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public Optional<Device> getById(Long id){
        return deviceRepository.findById(id);
    }
    public void create(Long id, Long userId, float maxConsumption){
        Device device = new Device();
        device.setId(id);
        device.setUserId(userId);
        device.setMaxConsumption(maxConsumption);
        deviceRepository.save(device);
    }
    public void delete(Long id){
        deviceRepository.deleteById(id);
    }
}
