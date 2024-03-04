package com.cezar.energydevices.service;

import com.cezar.energydevices.DTO.DeviceSaveEventDTO;
import com.cezar.energydevices.model.Device;
import com.cezar.energydevices.model.EndUser;
import com.cezar.energydevices.repository.DeviceRepository;
import com.cezar.energydevices.repository.EndUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cezar.energydevices.configuration.RabbitMQConfig;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    EndUserRepository endUserRepository;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    public List<Device> getAll(){
        return deviceRepository.findAll();
    }

    public Device getById(Long id){
        Optional<Device> device = deviceRepository.findById(id);
        return device.orElseThrow(()-> new EntityNotFoundException("There is no device with the given Id"));
    }

    @Transactional
    public Device create(String description, String address, float maxHourlyConsumption, EndUser owner) {
        Device device = new Device();
        device.setDescription(description);
        device.setAddress(address);
        device.setMaxHourlyConsumption(maxHourlyConsumption);
        device.setOwner(owner);
        Device result = deviceRepository.save(device);

        DeviceSaveEventDTO eventDTO = new DeviceSaveEventDTO();
        eventDTO.setCrud("CREATE");
        eventDTO.setId(result.getId());
        eventDTO.setDescription(result.getDescription());
        eventDTO.setAddress(result.getAddress());
        eventDTO.setMaxHourlyConsumption(result.getMaxHourlyConsumption());
        eventDTO.setOwnerId(result.getOwner().getId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName,"device.crud.create", eventDTO);

        return result;
    }

    public Device update(Device device, String description, String address, float maxHourlyConsumption, EndUser owner){
        device.setDescription(description);
        device.setAddress(address);
        device.setMaxHourlyConsumption(maxHourlyConsumption);
        device.setOwner(owner);
        Device result = deviceRepository.save(device);

        DeviceSaveEventDTO eventDTO = new DeviceSaveEventDTO();
        eventDTO.setCrud("UPDATE");
        eventDTO.setId(result.getId());
        eventDTO.setDescription(result.getDescription());
        eventDTO.setAddress(result.getAddress());
        eventDTO.setMaxHourlyConsumption(result.getMaxHourlyConsumption());
        eventDTO.setOwnerId(result.getOwner().getId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName,"device.crud.update", eventDTO);

        return result;
    }

    public void delete(long id){
        Optional<Device> optionalDevice = deviceRepository.findById(id);
        Device device = optionalDevice.orElseThrow(EntityNotFoundException::new);

        device.getOwner().getDevices().removeIf(device1 -> device1.getId().equals(id));
        endUserRepository.save(device.getOwner());
       // deviceRepository.delete(device);

        DeviceSaveEventDTO eventDTO = new DeviceSaveEventDTO();
        eventDTO.setCrud("DELETE");
        eventDTO.setId(id);
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName,"device.crud.delete", eventDTO);

    }
}
