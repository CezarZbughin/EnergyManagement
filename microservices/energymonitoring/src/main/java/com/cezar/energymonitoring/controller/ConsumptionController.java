package com.cezar.energymonitoring.controller;

import com.cezar.energymonitoring.DTO.ConsumptionEventDTO;
import com.cezar.energymonitoring.DTO.ConsumptionListDTO;
import com.cezar.energymonitoring.DTO.DeviceSaveEventDTO;
import com.cezar.energymonitoring.configuration.RabbitMQConfig;
import com.cezar.energymonitoring.model.Consumption;
import com.cezar.energymonitoring.model.Device;
import com.cezar.energymonitoring.repository.ConsumptionRepository;
import com.cezar.energymonitoring.repository.DeviceRepository;
import com.cezar.energymonitoring.service.ConsumptionNotificationService;
import com.cezar.energymonitoring.service.ConsumptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class ConsumptionController {
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    public DeviceRepository deviceRepository;
    @Autowired
    public ConsumptionRepository consumptionRepository;
    @Autowired
    public ConsumptionService consumptionService;
    @Autowired
    public ConsumptionNotificationService consumptionNotificationService;

    @GetMapping("/hello")
    public ResponseEntity<?> hello(HttpServletRequest request){
        return ResponseEntity.ok(new WebMessage("NO NO Hello"));
    }

    @GetMapping("api/v1/consumption/device/{deviceId}")
    public ResponseEntity<?> get(@PathVariable long deviceId){
        List<Consumption> consumptions = consumptionRepository.findByDevice_id(deviceId);
        return ResponseEntity.ok(new ConsumptionListDTO(consumptions));
    }

    @GetMapping("api/v1/consumption/device/{deviceId}/timestamp/{timestamp}")
    public ResponseEntity<?> get0(@PathVariable long deviceId, @PathVariable long timestamp){
        List<Consumption> consumptions = consumptionRepository.findByDevice_idAndTimestampAfter(deviceId, timestamp);
        return ResponseEntity.ok(new ConsumptionListDTO(consumptions));
    }

    @GetMapping("api/v1/consumption-last-hour/device/{deviceId}")
    public ResponseEntity<?> get1(@PathVariable long deviceId){
        List<Consumption> consumptions = consumptionService.getLastHourConsumption(deviceId);
        return ResponseEntity.ok(new ConsumptionListDTO(consumptions));
    }

    @GetMapping("test")
    public ResponseEntity<?> test() throws InterruptedException {
        Device device = new Device();
        device.setId(1L);
        device.setUserId(2L);
        device.setMaxConsumption(400);
        deviceRepository.save(device);

        consumptionService.create(1L, Instant.now().toEpochMilli(), 100);
        Thread.sleep(1000);
        consumptionService.create(1L, Instant.now().toEpochMilli(), 100);
        Thread.sleep(1000);
        consumptionService.create(1L, Instant.now().toEpochMilli(), 100);
        Thread.sleep(1000);
        consumptionService.create(1L, Instant.now().toEpochMilli(), 100);
        Thread.sleep(1000);
        consumptionService.create(1L, Instant.now().toEpochMilli(), 100);
        Thread.sleep(1000);

        List<Consumption> r = consumptionRepository.findByDevice_id(1L);
        return ResponseEntity.ok(r);
    }

    @GetMapping("test2")
    public ResponseEntity<?> test2(){
        //consumptionNotificationService.sendNotification(2L,2L);
        return ResponseEntity.ok("OK i guess!");
    }
}
