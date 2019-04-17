package com.pedropb.kaizen.users.api.in;

public class UpdateUser {
    public String name;
    public String email;

    public UpdateUser(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
