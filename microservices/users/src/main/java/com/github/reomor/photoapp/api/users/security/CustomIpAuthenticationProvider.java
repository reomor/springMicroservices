package com.github.reomor.photoapp.api.users.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomIpAuthenticationProvider implements AuthenticationProvider {
    private Set<String> whitelist = new HashSet<>();

    public CustomIpAuthenticationProvider() {
        whitelist.add("127.0.0.1");
        whitelist.add("10.0.75.1");
        whitelist.add("fe80::191e:3bd2:e41f:5622%18");
        whitelist.add("192.168.1.17");
        whitelist.add("fe80::e9a2:9c6f:45b8:47c6%13");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String userIp = details.getRemoteAddress();
        if (!whitelist.contains(userIp)) {
            throw new BadCredentialsException("Invalid IP Address");
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
