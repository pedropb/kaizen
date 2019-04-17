package com.pedropb.kaizen.users.infrastructure;

import com.google.gson.Gson;
import com.pedropb.kaizen.users.api.in.CreateUser;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.IOException;
import java.net.ServerSocket;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class UsersApplicationIntegrationTest {

    private static Gson gson;

    @BeforeAll
    static void setup() throws IOException {
        int port = new ServerSocket(0).getLocalPort();
        gson = new Gson();
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
    void post_when_user_does_not_exist_returns_200_and_creates_user() {
        CreateUser createUser = new CreateUser("John", "john@gmail.com");
        given()
                .body(gson.toJson(createUser))
        .when()
                .post("/")
        .then()
                .statusCode(200)
                .body("name", equalTo("John"))
                .body("email", equalTo("john@gmail.com"))
                .body("id", any(String.class));
    }

    @Test
    void get_root_path_returns_200_and_empty_array() {
        when().get("/").then().statusCode(200).body("$", hasSize(0));
    }


}