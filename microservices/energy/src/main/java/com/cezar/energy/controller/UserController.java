package com.cezar.energy.controller;

import com.cezar.energy.DTO.*;
import com.cezar.energy.configuration.JwtUtil;
import com.cezar.energy.service.EndUserService;
import com.cezar.energy.model.EndUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    EndUserService endUserService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/hello")
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok(new WebMessage("Hello, =(("));
    }


    @GetMapping("/api/v1/user")
    public ResponseEntity<?> getAll(){
        UserListDTO userListDTO = new UserListDTO(endUserService.getAll());
        return ResponseEntity.ok(userListDTO);
    }

    @GetMapping("/api/v1/user/id/{id}")
    public ResponseEntity<?> get(@PathVariable long id){
        try {
            return ResponseEntity.ok(endUserService.getById(id));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new WebMessage("There is no user with id: " + id + "."));
        }
    }

    @GetMapping("/api/v1/user/username/{username}")
    public ResponseEntity<?> get(@PathVariable String username){
        try {
            return ResponseEntity.ok(endUserService.getByUsername(username));
        } catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new WebMessage("There is no user with id: " + username + "."));
        }
    }

    @PostMapping("/api/v1/validate")
    public ResponseEntity<?> getByUsernameAndPassword(@RequestBody AuthDTO authDAO){
        try {
            EndUser user = endUserService.getByUsernameAndPassword(authDAO.getUsername(), authDAO.getPassword());
            return ResponseEntity.ok(new AuthResponse(true, user.getRole(), user.getId()));
        } catch (EntityNotFoundException e){
            return  ResponseEntity.ok(new AuthResponse(false, "", -1));
        }
    }

    @PostMapping("/api/v1/auth")
    public ResponseEntity<?> auth(@RequestBody LoginRequestDTO loginRequestDTO){
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
            String username = authentication.getName();
            EndUser user = endUserService.getByUsername(username);
            String token = jwtUtil.createToken(user);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(username, token, user.getId(), user.getRole());
            return ResponseEntity.ok(loginResponseDTO);
        } catch (BadCredentialsException e){
            WebMessage errorResponse = new WebMessage("Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            WebMessage errorResponse = new WebMessage("Something went wrong");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("api/v1/user")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody UserDTO userCreateDTO){
        EndUser user = endUserService.create(
                userCreateDTO.getName(),
                userCreateDTO.getUsername(),
                userCreateDTO.getPassword(),
                userCreateDTO.getRole(),
                request.getHeader("Authorization")
        );
        return ResponseEntity.ok(user);
    }

    @PutMapping("api/v1/user/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody UserDTO userDTO) {
        try {
            EndUser user = endUserService.getById(id);
            user = endUserService.update(
                    user,
                    userDTO.getName(),
                    userDTO.getUsername(),
                    userDTO.getPassword(),
                    userDTO.getRole()
            );
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new WebMessage("There is no user with id: " + id + "."));
        }
    }

    @DeleteMapping("api/v1/user/{id}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable long id){
        try {
            endUserService.delete(id, request.getHeader("Authorization"));
            return ResponseEntity.ok(new WebMessage("User deleted."));
        } catch( EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new WebMessage("There is no user with id: " + id + "."));
        }
    }

}
