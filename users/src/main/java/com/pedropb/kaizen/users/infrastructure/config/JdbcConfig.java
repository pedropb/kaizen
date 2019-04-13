package com.pedropb.kaizen.users.infrastructure.config;

public interface JdbcConfig {
    public String getDriverClass();
    public String getJdbcUrl();
    public String getUser();
    public String getPassword();
}
