package com.pedropb.kaizen.users.infrastructure.persistence;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.pedropb.kaizen.users.infrastructure.persistence.config.JdbcConfig;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

public class PooledDataSourceFactory {
    static public DataSource create(JdbcConfig config)  {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(config.getDriverClass());
        } catch (PropertyVetoException e) {
            System.err.println("Could not set the driver class for C3P0 ComboPooledDataSource");
            throw new RuntimeException(e.getCause());
        }
        cpds.setJdbcUrl(config.getJdbcUrl());
        cpds.setUser(config.getUser());
        cpds.setPassword(config.getPassword());
        return cpds;
    }
}
