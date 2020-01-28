package com.github.reomor.photoapp.api.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.reomor.photoapp.api.users.service.UserService;
import com.github.reomor.photoapp.api.users.shared.UserDto;
import com.github.reomor.photoapp.api.users.ui.model.LoginRequestModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Аутентификационный фильтр
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Environment environment;
    private final UserService userService;

    public AuthenticationFilter(
            Environment environment,
            UserService userService,
            AuthenticationManager authenticationManager
    ) {
        this.environment = environment;
        this.userService = userService;
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            final LoginRequestModel credentials = new ObjectMapper()
                    .readValue(
                            request.getInputStream(),
                            LoginRequestModel.class
                    );
            return getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    credentials.getEmail(),
                                    credentials.getPassword(),
                                    new ArrayList<>()
                            )
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException, ServletException {
        String userName = ((User) authentication.getPrincipal()).getUsername();
        final UserDto userDto = userService.getUserDetailsByEmail(userName);
        final String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(environment.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();
        response.addHeader("token", token);
        response.addHeader("userId", userDto.getUserId());
    }
}
