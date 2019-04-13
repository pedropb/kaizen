package com.pedropb.kaizen.users.domain;

import com.google.auto.value.AutoValue;

import java.util.Optional;
import java.util.Set;

@AutoValue
public abstract class UserQuery {

    public abstract Optional<Set<String>> idIn();
    public abstract Optional<String> nameStartWith();
    public abstract Optional<Set<String>> emailIn();

    public static UserQuery create(Optional<Set<String>> idIn, Optional<String> nameStartWith, Optional<Set<String>> emailIn) {
        return builder()
                .idIn(idIn)
                .nameStartWith(nameStartWith)
                .emailIn(emailIn)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_UserQuery.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder idIn(Optional<Set<String>> idIn);

        public abstract Builder nameStartWith(Optional<String> nameStartWith);

        public abstract Builder emailIn(Optional<Set<String>> emailIn);

        public abstract UserQuery build();
    }
}
