package com.github.reomor.photoapp.api.users.controllers;

import com.github.reomor.photoapp.api.users.service.UserService;
import com.github.reomor.photoapp.api.users.shared.UserDto;
import com.github.reomor.photoapp.api.users.ui.model.CreateUserRequest;
import com.github.reomor.photoapp.api.users.ui.model.CreateUserResponse;
import com.github.reomor.photoapp.api.users.ui.model.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private Environment environment;
    @Autowired
    private UserService userService;

    @GetMapping("/status/check")
    public String getStatus() {
        return "Working on:\n"
                + "port is:\t" + environment.getProperty("local.server.port") + "\n"
                + "token is:\t" + environment.getProperty("token.secret");
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        final UserDto userDto = modelMapper.map(userRequest, UserDto.class);
        final UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createdUser, CreateUserResponse.class));
    }

    @GetMapping(
            value = "/{userId",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {
        final UserDto userDto = userService.getUserById(userId);
        final UserResponseModel responseModel = new ModelMapper().map(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}
