package com.github.reomor.appws.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public String getUsersByParam(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false) String limit
    ) {
        return "get user was called with page(" + page + "), limit(" + limit + ")";
    }

    @GetMapping(
            path = "/{userId}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public WebUser getUserById(@PathVariable Integer userId) {
        WebUser webUser = new WebUser(userId, "Ivan", "Ivanov", "a@ya.ru");
        webUser.setUserId(1);
        return webUser;
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
