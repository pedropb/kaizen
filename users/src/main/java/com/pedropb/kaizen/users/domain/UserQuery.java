package com.pedropb.kaizen.users.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserQuery {

    public static Builder builder() {
        return new AutoValue_UserQuery.Builder();
    }

    public static UserQuery create() {
        return builder()
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract UserQuery build();
    }
}
