package com.pedropb.kaizen.users.domain.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.Set;

import static java.util.Collections.emptySet;

@AutoValue
public abstract class UserQuery {

    @Nullable
    public abstract Set<String> idIn();
    @Nullable
    public abstract String nameStartWith();
    @Nullable
    public abstract Set<String> emailIn();

    public static UserQuery create(Set<String> idIn, String nameStartWith, Set<String> emailIn) {
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

        public abstract Builder idIn(Set<String> idIn);

        public abstract Builder nameStartWith(String nameStartWith);

        public abstract Builder emailIn(Set<String> emailIn);

        public abstract UserQuery build();

        public static Builder create() {
            return new AutoValue_UserQuery.Builder()
                    .idIn(emptySet())
                    .nameStartWith(null)
                    .emailIn(emptySet());
        }

    }
}
