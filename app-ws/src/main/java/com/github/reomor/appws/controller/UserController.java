package com.github.reomor.appws.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public String getUsersByParam(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false) String limit
    ) {
        return "get user was called with page(" + page + "), limit(" + limit +")";
    }

    @GetMapping(path = "/{userId}")
    public String getUserById(@PathVariable String userId) {
        return "get user was called with userId(" + userId + ")";
    }

    @PostMapping
    public String createUser() {
        return "create user was called";
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
