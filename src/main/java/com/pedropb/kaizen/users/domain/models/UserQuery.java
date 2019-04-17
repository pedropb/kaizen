package com.pedropb.kaizen.users.domain.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.Set;

@AutoValue
public abstract class UserQuery {

    @Nullable
    public abstract Set<String> idIn();
    @Nullable
    public abstract String nameStartsWith();
    @Nullable
    public abstract Set<String> emailIn();

    public static UserQuery create(Set<String> idIn, String nameStartsWith, Set<String> emailIn) {
        return builder()
                .idIn(idIn)
                .nameStartsWith(nameStartsWith)
                .emailIn(emailIn)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_UserQuery.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder idIn(Set<String> idIn);

        public abstract Builder emailIn(Set<String> emailIn);

        public abstract Builder nameStartsWith(String nameStartsWith);

        public abstract UserQuery build();

    }
}
