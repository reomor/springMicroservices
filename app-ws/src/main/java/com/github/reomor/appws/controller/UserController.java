package com.github.reomor.appws.controller;

import com.github.reomor.appws.exception.MyCustomException;
import com.github.reomor.appws.exception.OneMoreCustomException;
import com.github.reomor.appws.model.request.UpdateUserDetailsRequestModel;
import com.github.reomor.appws.model.request.UserDetailsRequestModel;
import com.github.reomor.appws.model.response.WebUserModel;
import com.github.reomor.appws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    private Map<UUID, WebUserModel> users = new HashMap<>();

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
                    MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<WebUserModel> getUserById(@PathVariable UUID userId) {
        // generate error manually
        if (new Random().nextInt() % 3 == 0) {
            throw new MyCustomException("My custom exception");
        }
        if (new Random().nextInt() % 3 == 1) {
            throw new OneMoreCustomException("One more custom exception");
        }
        if (users.containsKey(userId)) {
            return new ResponseEntity<>(users.get(userId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<WebUserModel> createUser(
            @Valid @RequestBody UserDetailsRequestModel userDetails
    ) {
        final WebUserModel webUserModel = userService.createUser(userDetails);
        return new ResponseEntity<>(webUserModel, HttpStatus.CREATED);
    }

    @PutMapping(
            path = "/{userId}",
            consumes = {
                    MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<WebUserModel> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserDetailsRequestModel userDetails
    ) {
        final WebUserModel user = users.get(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final String userDetailsFirstName = userDetails.getFirstName();
        if (userDetailsFirstName != null && !userDetailsFirstName.isBlank()) {
            user.setFirstName(userDetailsFirstName);
        }
        final String userDetailsLastName = userDetails.getLastName();
        if (userDetailsLastName != null && !userDetailsLastName.isBlank()) {
            user.setLastName(userDetailsLastName);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        users.remove(userId);
        return ResponseEntity.noContent().build();
    }
}
