package com.pedropb.kaizen.users.api.out;

import java.util.Objects;

public class UserData {
    public String id;
    public String name;
    public String email;

    public UserData(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData)) return false;
        UserData that = (UserData) o;
        return this.id.equals(that.id) &&
                this.name.equals(that.name) &&
                this.email.equals(that.email);
    }
}
