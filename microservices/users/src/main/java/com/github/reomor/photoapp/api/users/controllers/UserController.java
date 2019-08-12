package com.github.reomor.photoapp.api.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private Environment environment;

    @GetMapping("/status/check")
    public String getStatus() {
        return "Working on: " + environment.getProperty("local.server.port");
    }
}
