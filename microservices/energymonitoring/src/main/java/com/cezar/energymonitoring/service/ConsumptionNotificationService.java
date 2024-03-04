package com.cezar.energymonitoring.service;

import com.cezar.energymonitoring.DTO.NotificationDTO;
import com.cezar.energymonitoring.controller.WebMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ConsumptionNotificationService {
    @Autowired
    public SimpMessagingTemplate template;
    public void sendNotification(long userId, long deviceId, float maxConsumption, double actualConsumption) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(userId);
        notificationDTO.setDeviceId(deviceId);
        notificationDTO.setMessage("max: " + maxConsumption + ", actual: " + actualConsumption);
        template.convertAndSend("/topic/llll/" + userId, notificationDTO);
    }

}
