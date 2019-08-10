package com.github.reomor.appws.controller;

import com.github.reomor.appws.controller.model.UserDetailsRequestModel;
import com.github.reomor.appws.controller.model.WebUserModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<WebUserModel> getUserById(@PathVariable Integer userId) {
        if (userId % 2 == 1) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        WebUserModel webUserModel = new WebUserModel(userId, "Ivan", "Ivanov", "a@ya.ru");
        webUserModel.setUserId(1);
        return new ResponseEntity<>(webUserModel, HttpStatus.OK);
    }

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<WebUserModel> createUser(
            @Valid @RequestBody UserDetailsRequestModel userDetails
    ) {
        WebUserModel webUserModel = new WebUserModel();
        webUserModel.setUserId(666);
        webUserModel.setEmail(userDetails.getEmail());
        webUserModel.setFirstName(userDetails.getFirstName());
        webUserModel.setLastName(userDetails.getLastName());
        return new ResponseEntity<>(webUserModel, HttpStatus.CREATED);
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
