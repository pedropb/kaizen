package com.pedropb.kaizen.users.domain;

import com.google.auto.value.AutoValue;

import java.util.UUID;

@AutoValue
public abstract class User {
    public abstract String id();
    public abstract String name();
    public abstract String email();

    public static User create(String id, String name, String email) {
        return builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_User.Builder();
    }

    public static Builder testBuilder() {
        return builder()
                .id(UUID.randomUUID().toString())
                .name(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString());
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract Builder email(String email);

        public abstract User build();
    }
}
