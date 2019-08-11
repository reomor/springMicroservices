package com.github.reomor.appws.common;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Utils {
    public UUID generateUserId() {
        return UUID.randomUUID();
    }
}
