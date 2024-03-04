package com.cezar.energymonitoring.event;

import com.cezar.energymonitoring.DTO.DeviceSaveEventDTO;
import com.cezar.energymonitoring.service.DeviceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceSaveEventReceiver {
    @Autowired
    private DeviceService deviceService;

    @RabbitListener(queues = {"device-save-event-queue"})
    public void consumeSaveMessage(DeviceSaveEventDTO event){
        switch (event.getCrud()){
            case "CREATE": {
                deviceService.create(event.getId(), event.getOwnerId(), event.getMaxHourlyConsumption());
                System.out.println("event: create");
                break;
            }
            case "DELETE": {
                deviceService.delete(event.getId());
                System.out.println("event: delete");
                break;
            }
        }
    }
}