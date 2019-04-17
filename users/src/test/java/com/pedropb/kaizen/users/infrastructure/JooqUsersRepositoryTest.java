package com.pedropb.kaizen.users.infrastructure;

import com.pedropb.kaizen.users.domain.UsersRepository;
import com.pedropb.kaizen.users.domain.UsersRepositoryTest;
import com.pedropb.kaizen.users.infrastructure.persistence.config.StubConfig;
import com.pedropb.kaizen.users.infrastructure.persistence.JooqUsersRepository;
import com.pedropb.kaizen.users.infrastructure.persistence.PooledDataSourceFactory;
import infrastructure.generated.jooq.Kaizen;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import javax.sql.DataSource;
import java.util.stream.Collectors;

class JooqUsersRepositoryTest extends UsersRepositoryTest {
    private static UsersRepository jooqUsersRepository;
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

        jooqUsersRepository = new JooqUsersRepository(dataSource);
    }

    @AfterEach
    void setUp() {
        DSLContext context = DSL.using(dataSource, SQLDialect.H2);
        context.execute("SET REFERENTIAL_INTEGRITY FALSE");
        context.batch(Kaizen.KAIZEN.getTables()
                                   .stream()
                                   .map(context::truncate)
                                   .collect(Collectors.toList()))
               .execute();
        context.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}