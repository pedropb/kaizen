package com.pedropb.kaizen.users.infrastructure.persistence.config;

public class StubConfig extends DevConfig {
    @Override
    public String getJdbcUrl() {
        return "jdbc:h2:mem:test_users;MODE=MySQL;DB_CLOSE_DELAY=-1";
    }
}
