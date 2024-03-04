package com.cezar.energydevices.service;

import com.cezar.energydevices.model.Device;
import com.cezar.energydevices.model.EndUser;
import com.cezar.energydevices.repository.DeviceRepository;
import com.cezar.energydevices.repository.EndUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EndUserService {
    @Autowired
    EndUserRepository endUserRepository;
    @Autowired
    DeviceRepository deviceRepository;
    public EndUser getById(Long id){
        Optional<EndUser> user = endUserRepository.findById(id);
        return user.orElseThrow(()-> new EntityNotFoundException("There is no user with the given Id"));
    }

    public List<EndUser> getAll() {
        return endUserRepository.findAll();
    }
    public EndUser getByUsername(String username){
        return endUserRepository.findByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("There is no user with the given username"));
    }

    public EndUser create(Long id, String username){
        EndUser user = new EndUser();
        user.setId(id);
        user.setUsername(username);
        user.setDevices(new ArrayList<>());
        return endUserRepository.save(user);
    }

    public void delete(long id) {
        Optional<EndUser> optionalUser = endUserRepository.findById(id);
        EndUser user = optionalUser.orElseThrow(EntityNotFoundException::new);
        user.setDevices(new ArrayList<>());
        endUserRepository.save(user);
        endUserRepository.delete(user);
    }

    public void addInitialData(){
        Optional<EndUser> oAdmin = endUserRepository.findByUsername("admin");
        if(oAdmin.isEmpty()){
            EndUser admin = new EndUser();
            admin.setId(1L);
            admin.setUsername("admin");
            endUserRepository.save(admin);
        }

        Optional<EndUser> oUser = endUserRepository.findByUsername("user");
        if(oUser.isEmpty()){
            EndUser user = new EndUser();
            user.setId(2L);
            user.setUsername("user");
            endUserRepository.save(user);
        }
    }


}
