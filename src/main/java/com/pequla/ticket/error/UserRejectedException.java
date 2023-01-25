package com.pequla.ticket.error;

import lombok.Getter;

@Getter
public class UserRejectedException extends RuntimeException {
    private final Type type;

    public UserRejectedException(Type type) {
        this.type = type;
    }

    public enum Type {
        NOT_FOUND, BAD_PASSWORD, NOT_ADMIN, TOKEN_EXPIRED, NOT_VERIFIED_EMAIL
    }
}
