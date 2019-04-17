package com.pedropb.kaizen.users.infrastructure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.out.UserData;
import com.pedropb.kaizen.users.infrastructure.persistence.PooledDataSourceFactory;
import com.pedropb.kaizen.users.infrastructure.persistence.config.StubConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class UsersApplicationIntegrationTest {

    private static Gson gson;
    private static Flyway flyway;
    private static Type LIST_USER_DATA = new TypeToken<List<UserData>>() {}.getType();

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
    void get_when_no_users_returns_200_and_empty_array() {
        when().get("/").then().statusCode(200).body("$", hasSize(0));
    }

    @Test
    void get_when_users_returns_200_and_array_with_users() {
        UserData alice = createUser("Alice", "alice@gmail.com");
        UserData bob = createUser("Bob", "bob@gmail.com");
        UserData chris = createUser("Chris", "chris@gmail.com");

        Response response = get("/");
        response.then()
                .statusCode(200)
                .body("$", hasSize(3));

        assertThat(gson.fromJson(response.body().print(), LIST_USER_DATA), containsInAnyOrder(bob, alice, chris));
    }

    private UserData createUser(String name, String email) {
        CreateUser createUser = new CreateUser(name, email);
        return gson.fromJson(given().body(gson.toJson(createUser))
                                    .post("/")
                                    .andReturn()
                                    .body()
                                    .print(),
                             UserData.class);
    }

}