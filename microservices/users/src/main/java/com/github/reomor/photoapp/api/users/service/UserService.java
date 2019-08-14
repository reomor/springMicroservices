package com.github.reomor.photoapp.api.users.service;

import com.github.reomor.photoapp.api.users.shared.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
}
