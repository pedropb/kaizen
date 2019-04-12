package com.pedropb.kaizen.users.infrastructure;

import spark.Spark;

import java.util.Arrays;

public class SparkServer {

    public static final int SERVER_PORT = 4657;

    public SparkServer() {
        Spark.port(SERVER_PORT);
    }

    public void register(Resource... resources) {
        Arrays.stream(resources)
              .forEach(Resource::configure);
    }

    public void stop() {
        Spark.stop();
    }
}
