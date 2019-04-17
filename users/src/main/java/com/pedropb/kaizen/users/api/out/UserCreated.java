package com.pedropb.kaizen.users.api.out;

public class UserCreated {
    public String id;
    public String name;
    public String email;

    public UserCreated(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
