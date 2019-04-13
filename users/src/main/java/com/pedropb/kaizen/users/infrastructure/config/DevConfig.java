package com.pedropb.kaizen.users.infrastructure.config;

public class DevConfig implements JdbcConfig {
    @Override
    public String getDriverClass() {
        return "org.h2.Driver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:h2:mem:users;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    }

    @Override
    public String getUser() {
        return "sa";
    }

    @Override
    public String getPassword() {
        return "";
    }
}
