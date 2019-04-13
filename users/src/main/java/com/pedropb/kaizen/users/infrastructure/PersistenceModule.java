package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.AbstractModule;
import com.pedropb.kaizen.users.infrastructure.config.DevConfig;

import javax.sql.DataSource;

public class PersistenceModule extends AbstractModule {

    @Override
    public void configure() {
        bind(DataSource.class).toInstance(PooledDataSourceFactory.create(new DevConfig()));
    }
}
