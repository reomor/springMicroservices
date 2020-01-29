package com.github.reomor.photoapp.api.users.service;

import com.github.reomor.photoapp.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service to get users
 */
public interface UserService extends UserDetailsService {
    /**
     * Create new user
     *
     * @param userDto
     * @return
     */
    UserDto createUser(UserDto userDto);

    /**
     * Get user by email
     *
     * @param email
     * @return
     */
    UserDto getUserDetailsByEmail(String email);

    /**
     * Get user by UUID
     *
     * @param userId
     * @return
     */
    UserDto getUserById(String userId);
}
