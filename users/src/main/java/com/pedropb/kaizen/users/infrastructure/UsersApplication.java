package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.flywaydb.core.Flyway;
import spark.Spark;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class UsersApplication {

    private static void registerResources(Resource... resources) {
        Arrays.stream(resources)
              .forEach(Resource::configure);
    }

    private static void configureLogging() {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tLZ %4$-6s %2$s %5$s%6$s%n");

        try {
            InputStream logConfig = UsersApplication.class.getResourceAsStream("/logging.properties");
            LogManager.getLogManager().readConfiguration(logConfig);
            Logger.getGlobal().log(Level.CONFIG, "Logging configuration loaded.");
        } catch (Exception ex) {
            Logger.getAnonymousLogger().severe("Could not load logging properties file");
            Logger.getAnonymousLogger().severe(ex.getMessage());
        }
    }

    private static void migrateDatabase(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new UsersModule(), new PersistenceModule());
        configureLogging();
        migrateDatabase(injector.getInstance(DataSource.class));
        registerResources(injector.getInstance(UsersResources.class));
        Spark.init();
    }
}