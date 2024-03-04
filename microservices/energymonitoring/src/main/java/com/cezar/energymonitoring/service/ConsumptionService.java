package com.cezar.energymonitoring.service;

import com.cezar.energymonitoring.model.Consumption;
import com.cezar.energymonitoring.model.Device;
import com.cezar.energymonitoring.repository.ConsumptionRepository;
import com.cezar.energymonitoring.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ConsumptionService {
    @Autowired
    ConsumptionRepository consumptionRepository;
    @Autowired
    DeviceService deviceService;
    @Autowired
    ConsumptionNotificationService consumptionNotificationService;

    public Consumption create(Long deviceId, long timestamp, float measure){
        Device device = deviceService.getById(deviceId).orElseThrow(EntityNotFoundException::new);
        Consumption consumption = new Consumption();
        consumption.setDevice(device);
        consumption.setTimestamp(timestamp);
        consumption.setMeasure(measure);

        var result = consumptionRepository.save(consumption);

        List<Consumption> consumptions = this.getLastHourConsumption(deviceId);
        double sum = consumptions.stream().mapToDouble(Consumption::getMeasure).sum();
        if(sum > device.getMaxConsumption()){
            consumptionNotificationService.sendNotification(device.getUserId(), deviceId, device.getMaxConsumption(), sum);
        }

        return result;
    }
    public List<Consumption> getLastHourConsumption(long deviceId){
        long oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS).toEpochMilli();
        return consumptionRepository.findByDevice_idAndTimestampAfter(deviceId, oneHourAgo);
    }
}
