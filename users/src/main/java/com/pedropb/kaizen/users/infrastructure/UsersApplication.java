package com.pedropb.kaizen.users.infrastructure;


public class UsersApplication {

    private static void run() {
        SparkServer spark = new SparkServer();
        spark.register(new UsersResources());
    }

    public static void main(String[] args) {
        run();
    }
}