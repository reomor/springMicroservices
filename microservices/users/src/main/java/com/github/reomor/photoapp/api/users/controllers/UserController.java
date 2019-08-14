package com.github.reomor.photoapp.api.users.controllers;

import com.github.reomor.photoapp.api.users.ui.model.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private Environment environment;

    @GetMapping("/status/check")
    public String getStatus() {
        return "Working on: " + environment.getProperty("local.server.port");
    }

    @PostMapping
    public String createUser(@Valid @RequestBody CreateUserRequest userRequest) {
        return "Create user method";
    }
}
