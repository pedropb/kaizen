package com.pedropb.kaizen.users.api.in;

public class CreateUser {
    public String name;
    public String email;

    public CreateUser(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
