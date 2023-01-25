package com.pequla.ticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pequla.ticket.entity.AppUser;
import com.pequla.ticket.model.CreateModel;
import com.pequla.ticket.model.LoginModel;
import com.pequla.ticket.model.UserModel;
import com.pequla.ticket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public Page<AppUser> getUsersPaged(@RequestParam String token, Pageable pageable) throws IOException {
        return service.getAllUsers(token, pageable);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AppUser> getUsersPaged(@RequestParam String token, @PathVariable Integer id) throws IOException {
        return ResponseEntity.of(service.getUserById(token, id));
    }

    @PostMapping(path = "/login")
    public HashMap<String, String> login(@RequestBody LoginModel model) throws JsonProcessingException {
        return service.login(model);
    }

    @GetMapping("/self")
    public UserModel getSelfUser(@RequestParam String token) throws IOException {
        return service.getSelfUser(token);
    }

    @PostMapping
    public UserModel createUser(@RequestBody CreateModel model) {
        return service.createUser(model);
    }

    @PutMapping
    public UserModel updateUser(@RequestParam String token, @RequestBody UserModel model) throws IOException {
        return service.updateUser(token, model);
    }

    @PutMapping(path = "/verify/{token}")
    public UserModel verifyEmail(@PathVariable String token) {
        return service.verifyUser(token);
    }
}
