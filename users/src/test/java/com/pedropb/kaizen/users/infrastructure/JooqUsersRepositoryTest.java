package com.pedropb.kaizen.users.infrastructure;

import com.pedropb.kaizen.users.domain.UsersRepository;
import com.pedropb.kaizen.users.domain.UsersRepositoryTest;
import com.pedropb.kaizen.users.infrastructure.config.DevConfig;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;


class JooqUsersRepositoryTest extends UsersRepositoryTest {
    public static final String JDBC_URL = "jdbc:h2:mem:test_users;MODE=MySQL;DB_CLOSE_DELAY=-1";

    private UsersRepository jooqUsersRepository;
    private static DataSource dataSource;

    @Override
    public UsersRepository repository() {
        return jooqUsersRepository;
    }

    @BeforeAll
    static void setupDatabase() {
        Flyway flyway = new Flyway();
        dataSource = PooledDataSourceFactory.create(new StubConfig());
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    @BeforeEach
    void setUp() {
        jooqUsersRepository = new JooqUsersRepository(dataSource);
    }

    private static class StubConfig extends DevConfig {
        @Override
        public String getJdbcUrl() {
            return JDBC_URL;
        }
    }
}