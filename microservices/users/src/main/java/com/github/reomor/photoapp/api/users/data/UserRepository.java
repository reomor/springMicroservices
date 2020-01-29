package com.github.reomor.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;

/**
 * Crud repository for user
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    /**
     * Get user by email
     *
     * @param email user email
     * @return object {@link UserEntity} or null
     */
    UserEntity findByEmail(String email);

    /**
     * Get user by id
     *
     * @param userId user UUID
     * @return object {@link UserEntity} or null
     */
    UserEntity findByUserId(String userId);
}
