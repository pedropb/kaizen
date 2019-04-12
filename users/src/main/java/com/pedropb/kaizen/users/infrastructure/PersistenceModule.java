package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.AbstractModule;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

public class PersistenceModule extends AbstractModule {

    private final ComboPooledDataSource cpds;

    public PersistenceModule() throws PropertyVetoException {
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.h2.Driver");
        cpds.setJdbcUrl("jdbc:h2:mem:users;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        cpds.setUser("sa");
        cpds.setPassword("");
    }

    @Override
    public void configure() {
        bind(DataSource.class).toInstance(cpds);
    }
}
