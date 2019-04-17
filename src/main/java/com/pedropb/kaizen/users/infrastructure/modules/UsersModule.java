package com.pedropb.kaizen.users.infrastructure.modules;

import com.google.inject.AbstractModule;
import com.pedropb.kaizen.users.domain.UsersRepository;
import com.pedropb.kaizen.users.domain.UsersService;
import com.pedropb.kaizen.users.infrastructure.persistence.JooqUsersRepository;
import com.pedropb.kaizen.users.infrastructure.persistence.PooledDataSourceFactory;
import com.pedropb.kaizen.users.infrastructure.persistence.config.DevConfig;
import com.pedropb.kaizen.users.infrastructure.persistence.config.JdbcConfig;
import com.pedropb.kaizen.users.infrastructure.persistence.config.StubConfig;
import com.pedropb.kaizen.users.infrastructure.resources.UsersResources;

import javax.sql.DataSource;
import java.util.Optional;

public class UsersModule extends AbstractModule {

    @Override
    protected void configure() {
        configureDataSource();

        bind(JooqUsersRepository.class);
        bind(UsersService.class);
        bind(UsersResources.class);

        bind(UsersRepository.class).to(JooqUsersRepository.class);
    }

    private void configureDataSource() {
        JdbcConfig jdbcConfig;
        switch(Optional.ofNullable(System.getProperty("env")).orElse("dev")) {
            case "test":
                jdbcConfig = new StubConfig();
                break;
            case "dev":
            default:
                jdbcConfig = new DevConfig();
                break;
        }

        bind(DataSource.class).toInstance(PooledDataSourceFactory.create(jdbcConfig));
    }
}
