package com.cezar.energy.service;

import com.cezar.energy.controller.WebMessage;
import com.cezar.energy.model.EndUser;
import com.cezar.energy.repository.EndUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class EndUserService {
    @Autowired
    public EndUserRepository endUserRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Value("${microservice.devices.address}")
    private String devicesMicroserviceURL;

    public List<EndUser> getAll() {
        return endUserRepository.findAll();
    }
    public EndUser getByUsername(String username) {
        Optional<EndUser> user = endUserRepository.findByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("Username was not found"));
    }

    public EndUser getByUsernameAndPassword(String username, String password) {
        Optional<EndUser> optionalUser = endUserRepository.findByUsername(username);
        EndUser user = optionalUser.orElseThrow(() -> new EntityNotFoundException("Username was not found"));
        if(passwordEncoder.matches(password, user.getPassword())){
            return user;
        } else {
            throw new EntityNotFoundException("Username and password don't match");
        }
    }

    public EndUser getById(Long id) {
        Optional<EndUser> user = endUserRepository.findById(id);
        return user.orElseThrow(()-> new EntityNotFoundException("There is no user with the given Id"));
    }

    @Transactional
    public EndUser create(String name, String username, String password, String role, String authHeader) {
        EndUser user = new EndUser();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        EndUser newUser = endUserRepository.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<WebMessage> responseEntity =
                new RestTemplate().postForEntity(
                        devicesMicroserviceURL + "/api/v1/user/" + newUser.getId() + "/" + newUser.getUsername(),
                        requestEntity,
                        WebMessage.class
                );
        if(responseEntity.getStatusCode().isError()){
            endUserRepository.delete(newUser);
        }

        return newUser;
    }
    public EndUser update(EndUser user, String name, String username, String password, String role){
        user.setName(name);
        user.setUsername(username);
        if(!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setRole(role);
        return endUserRepository.save(user);
    }

    public void delete(long id, String authHeader){
        Optional<EndUser> oUser = endUserRepository.findById(id);
        EndUser user = oUser.orElseThrow(EntityNotFoundException::new);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = new RestTemplate().exchange(
                devicesMicroserviceURL + "/api/v1/user/" + user.getId(),
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        endUserRepository.delete(user);
    }

    public void addInitialData() {
        Optional<EndUser> oAdmin = endUserRepository.findByUsername("admin");
        if(oAdmin.isEmpty()){
            EndUser admin = new EndUser();
            admin.setId(1L);
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("root"));
            admin.setRole("ADMIN");
            endUserRepository.save(admin);
        }

        Optional<EndUser> oUser = endUserRepository.findByUsername("user");
        if(oUser.isEmpty()){
            EndUser user = new EndUser();
            user.setId(2L);
            user.setName("User");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("root"));
            user.setRole("USER");
            endUserRepository.save(user);
        }
    }

}
