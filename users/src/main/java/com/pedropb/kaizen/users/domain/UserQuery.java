package com.pedropb.kaizen.users.domain;

import com.google.auto.value.AutoValue;

import java.util.Optional;
import java.util.Set;

@AutoValue
public abstract class UserQuery {

    public abstract Optional<Set<String>> ids();
    public abstract Optional<Set<String>> names();
    public abstract Optional<String> email();

    public static UserQuery create(Optional<Set<String>> ids, Optional<Set<String>> names, Optional<String> email) {
        return builder()
                .ids(ids)
                .names(names)
                .email(email)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_UserQuery.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder ids(Optional<Set<String>> ids);

        public abstract Builder email(Optional<String> email);

        public abstract Builder names(Optional<Set<String>> names);

        public abstract UserQuery build();
    }
}
