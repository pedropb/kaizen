package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.Guice;
import com.google.inject.Injector;
import spark.Spark;

import java.util.Arrays;

public class UsersApplication {

    public static void registerResources(Resource... resources) {
        Arrays.stream(resources)
              .forEach(Resource::configure);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new UsersModule());
        registerResources(injector.getInstance(UsersResources.class));
        Spark.init();
    }
}