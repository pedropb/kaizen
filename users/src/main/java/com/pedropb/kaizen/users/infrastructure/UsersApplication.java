package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.Guice;

public class UsersApplication {

    private final SparkServer spark;

    public UsersApplication(SparkServer sparkServer) {
        spark = sparkServer;
    }

    public void configure() {
        spark.register(new UsersResources());
        Guice.createInjector(new UsersModule());
    }

    public static void main(String[] args) {
        UsersApplication app = new UsersApplication(new SparkServer());
        app.configure();
    }
}