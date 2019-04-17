package com.pedropb.kaizen.users.infrastructure.config;

public interface JdbcConfig {
    String getDriverClass();
    String getJdbcUrl();
    String getUser();
    String getPassword();
}
