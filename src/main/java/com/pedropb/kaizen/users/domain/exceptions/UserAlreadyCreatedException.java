package com.pedropb.kaizen.users.domain.exceptions;

public class UserAlreadyCreatedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Creating user failed probably because it already exists on the database.";
    }
}
