package com.pedropb.kaizen.users.infrastructure.modules;

import com.google.inject.AbstractModule;
import com.pedropb.kaizen.users.infrastructure.persistence.PooledDataSourceFactory;
import com.pedropb.kaizen.users.infrastructure.persistence.config.DevConfig;
import com.pedropb.kaizen.users.infrastructure.persistence.config.JdbcConfig;
import com.pedropb.kaizen.users.infrastructure.persistence.config.StubConfig;

import javax.sql.DataSource;

public class PersistenceModule extends AbstractModule {

    @Override
    public void configure() {
        JdbcConfig jdbcConfig;
        switch(System.getProperty("Environment")) {
            case "Development":
                jdbcConfig = new DevConfig();
                break;
            case "Test":
            default:
                jdbcConfig = new StubConfig();
                break;
        }

        bind(DataSource.class).toInstance(PooledDataSourceFactory.create(jdbcConfig));
    }
}
