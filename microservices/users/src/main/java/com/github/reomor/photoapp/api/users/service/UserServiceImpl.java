package com.github.reomor.photoapp.api.users.service;

import com.github.reomor.photoapp.api.users.data.AlbumsServiceClient;
import com.github.reomor.photoapp.api.users.data.UserEntity;
import com.github.reomor.photoapp.api.users.data.UserRepository;
import com.github.reomor.photoapp.api.users.shared.UserDto;
import com.github.reomor.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AlbumsServiceClient albumsServiceClient;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            AlbumsServiceClient albumsServiceClient
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.albumsServiceClient = albumsServiceClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        final UserEntity savedUser = userRepository.save(userEntity);
        return modelMapper.map(savedUser, UserDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto getUserDetailsByEmail(String email) {
        final UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new ModelMapper().map(userEntity, UserDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto getUserById(String userId) {
        final UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        final UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        List<AlbumResponseModel> albums;
        try {
            albums = albumsServiceClient.getAlbums(userId);
        } catch (Exception e) {
            logger.error("Error getting albums", e);
            albums = new ArrayList<>();
        }
        userDto.setAlbums(albums);

        return userDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        final UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }
}
