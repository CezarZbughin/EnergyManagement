package com.cezar.energydevices.controller;

import com.cezar.energydevices.model.EndUser;
import com.cezar.energydevices.service.EndUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
public class EndUserController {
    @Autowired
    EndUserService endUserService;

    @GetMapping("/api/v1/user/self")
    public ResponseEntity<?> getSelf(Principal principal){
      try{
            EndUser self = endUserService.getByUsername(principal.getName());
            return ResponseEntity.ok(self);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/api/v1/user/{id}/{username}")
    public ResponseEntity<?> createUser(@PathVariable Long id, @PathVariable String username){
        try {
            EndUser user = endUserService.create(id, username);
            return ResponseEntity.ok(new WebMessage("Success"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.ok(new WebMessage("Failed to add user"));
        }
    }

    @DeleteMapping("/api/v1/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        try {
            endUserService.delete(id);
            return ResponseEntity.ok(new WebMessage("Deleted"));
        } catch (EntityNotFoundException e){
            return ResponseEntity.ok(new WebMessage("Failed to delete"));
        }
    }

}
