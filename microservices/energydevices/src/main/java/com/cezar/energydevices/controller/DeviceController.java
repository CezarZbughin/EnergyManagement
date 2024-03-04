package com.cezar.energydevices.controller;

import com.cezar.energydevices.DTO.DeviceDTO;
import com.cezar.energydevices.DTO.DeviceListDTO;
import com.cezar.energydevices.model.Device;
import com.cezar.energydevices.model.EndUser;
import com.cezar.energydevices.service.DeviceService;
import com.cezar.energydevices.service.EndUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private EndUserService endUserService;

    @GetMapping("/hello")
    public ResponseEntity<?> hello(HttpServletRequest request){
        return ResponseEntity.ok(new WebMessage("Hello 4 -_-"));
    }

    @GetMapping("/api/v1/device")
    public ResponseEntity<?> getAll(HttpServletRequest request){
        DeviceListDTO deviceListDTO = new DeviceListDTO(deviceService.getAll());
        return ResponseEntity.ok(deviceListDTO);
    }

    @GetMapping("/api/v1/device/id/{id}")
    public ResponseEntity<?> get(HttpServletRequest request, @PathVariable long id){
        try {
            return ResponseEntity.ok(deviceService.getById(id));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new WebMessage("There is no device with id: " + id + "."));
        }
    }

    @PostMapping("api/v1/device")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody DeviceDTO deviceDTO){
        EndUser owner = endUserService.getById(deviceDTO.getOwnerId());
        Device device = deviceService.create(
                deviceDTO.getDescription(),
                deviceDTO.getAddress(),
                deviceDTO.getMaxHourlyConsumption(),
                owner
        );
        return ResponseEntity.ok(device);
    }

    @PutMapping("api/v1/device/{id}")
    public ResponseEntity<?> update(HttpServletRequest request, @PathVariable long id, @RequestBody DeviceDTO deviceDTO) {
        try {
            Device device = deviceService.getById(id);
            EndUser owner = endUserService.getById(deviceDTO.getOwnerId());
            Device newDevice = deviceService.update(
                    device,
                    deviceDTO.getDescription(),
                    deviceDTO.getAddress(),
                    deviceDTO.getMaxHourlyConsumption(),
                    owner
            );
            return ResponseEntity.ok(newDevice);
        } catch ( EntityNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new WebMessage("Device or User not found."));
        }
    }

    @DeleteMapping("api/v1/device/{id}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable long id){
        try {
            deviceService.delete(id);
            return ResponseEntity.ok(new WebMessage("User deleted."));
        } catch( EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new WebMessage("There is no device with id: " + id + "."));
        }
    }
}
