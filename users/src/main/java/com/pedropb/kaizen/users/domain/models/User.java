package com.pedropb.kaizen.users.domain.models;

import com.google.auto.value.AutoValue;

import java.util.Optional;
import java.util.UUID;

@AutoValue
public abstract class User {
    public abstract String id();
    public abstract String name();
    public abstract String email();
    @IgnoreHashEquals
    public abstract Optional<Integer> version();

    public static Builder builder() {
        return new $AutoValue_User.Builder();
    }

    public static Builder testBuilder() {
        return builder()
                .id(UUID.randomUUID().toString())
                .name(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString())
                .version(Optional.empty());
    }

    public abstract Builder toBuilder();

    public static User create(String id, String name, String email, Optional<Integer> version) {
        return builder()
                .id(id)
                .name(name)
                .email(email)
                .version(version)
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract Builder email(String email);

        public abstract Builder version(Optional<Integer> version);

        public abstract User build();
    }
}
