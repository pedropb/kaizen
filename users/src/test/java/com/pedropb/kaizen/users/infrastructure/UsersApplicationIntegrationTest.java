package com.pedropb.kaizen.users.infrastructure;

import com.google.gson.Gson;
import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.infrastructure.persistence.PooledDataSourceFactory;
import com.pedropb.kaizen.users.infrastructure.persistence.config.StubConfig;
import io.restassured.RestAssured;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class UsersApplicationIntegrationTest {

    private static Gson gson;
    private static Flyway flyway;

    @BeforeAll
    static void setup() throws IOException {
        System.setProperty("env", "test");
        int port = new ServerSocket(0).getLocalPort();
        gson = new Gson();
        Spark.port(port);
        RestAssured.port = port;

        UsersApplication.main(new String[]{});

        Spark.awaitInitialization();

        DataSource dataSource = PooledDataSourceFactory.create(new StubConfig());
        flyway = Flyway.configure()
                       .dataSource(dataSource)
                       .schemas("KAIZEN")
                       .load();
    }

    @AfterAll
    static void tearDown() {
        Spark.stop();
    }

    @BeforeEach
    void resetSchema() {
        flyway.clean();
        flyway.migrate();
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
                .body("name", is("John"))
                .body("email", is("john@gmail.com"))
                .body("id", any(String.class));
    }

    @Test
    void get_root_path_returns_200_and_empty_array() {
        when().get("/").then().statusCode(200).body("$", hasSize(0));
    }


}