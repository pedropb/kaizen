package com.pedropb.kaizen.users.infrastructure;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

public class UsersApplicationIntegrationTest {

    private static SparkServer sparkServer;
    private static UsersApplication usersApplication;

    @BeforeAll
    public static void setup() {
        sparkServer = new SparkServer();
        usersApplication = new UsersApplication(sparkServer);
        usersApplication.configure();
        Spark.awaitInitialization();

        RestAssured.port = sparkServer.SERVER_PORT;
    }

    @AfterAll
    public static void tearDown() {
        sparkServer.stop();
    }

    @Test
    public void get_root_path_returns_200_helloworld() {
        when().get("/").then().statusCode(200).body(containsString("Hello World"));
    }
}