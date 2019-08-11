package com.github.reomor.appws.controller;

import com.github.reomor.appws.controller.model.UpdateUserDetailsRequestModel;
import com.github.reomor.appws.controller.model.UserDetailsRequestModel;
import com.github.reomor.appws.controller.model.WebUserModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
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
        WebUserModel webUserModel = new WebUserModel();
        final UUID userId = UUID.randomUUID();
        webUserModel.setUserId(userId);
        webUserModel.setEmail(userDetails.getEmail());
        webUserModel.setFirstName(userDetails.getFirstName());
        webUserModel.setLastName(userDetails.getLastName());
        users.put(userId, webUserModel);
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
