package com.pedropb.kaizen.users.infrastructure;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.ServerSocket;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;

class UsersApplicationIntegrationTest {

    @BeforeAll
    static void setup() throws IOException, PropertyVetoException {
        int port = new ServerSocket(0).getLocalPort();
        Spark.port(port);
        RestAssured.port = port;

        UsersApplication.main(new String[]{});

        Spark.awaitInitialization();
    }

    @AfterAll
    static void tearDown() {
        Spark.stop();
    }

    @Test
    void get_root_path_returns_200_and_empty_array() {
        when().get("/").then().statusCode(200).body("$", hasSize(0));
    }
}