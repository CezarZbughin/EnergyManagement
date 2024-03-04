package com.cezar.energymonitoring.event;

import com.cezar.energymonitoring.DTO.ConsumptionEventDTO;
import com.cezar.energymonitoring.DTO.DeviceSaveEventDTO;
import com.cezar.energymonitoring.model.Consumption;
import com.cezar.energymonitoring.model.Device;
import com.cezar.energymonitoring.service.ConsumptionService;
import com.cezar.energymonitoring.service.DeviceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumptionEventReceiver {
    @Autowired
    private ConsumptionService consumptionService;

    @RabbitListener(queues = {"device-consumption-event-queue"})
    public void consumeConsumptionMessage(ConsumptionEventDTO event){
        consumptionService.create(event.getDeviceId(), event.getTimestamp(), event.getMeasurementValue());
        System.out.println("Event: Consumption. Created row in DB!");
    }
}