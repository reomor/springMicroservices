package com.github.reomor.appws.model.response;

import java.time.LocalDateTime;

public class CustomErrorMessage {
    private String message;
    private LocalDateTime localDateTime;

    public CustomErrorMessage(String message, LocalDateTime localDateTime) {
        this.message = message;
        this.localDateTime = localDateTime;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
