package com.pedropb.kaizen.users.infrastructure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.in.UpdateUser;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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
    void post_with_incorrect_payload_returns_400() {
        given()
                .body("{\"unknownProperty\": true }")
        .when()
                .post("/")
        .then()
                .statusCode(400)
                .body("error", is(true))
                .body("exception", is("InvalidDtoException"));
    }

    @Test
    void get_when_no_users_returns_200_and_empty_array() {
        when().get("/").then().statusCode(200).body("$", hasSize(0));
    }

    @Test
    void get_with_path_param_when_user_exists_returns_200_and_user_data() {
        UserData alice = createUser("Alice", "alice@gmail.com");

        Response response = get("/{id}", alice.id);
        assertThat(response.statusCode(), is(200));
        assertThat(response.as(UserData.class), is(alice));
    }

    @Test
    void get_with_path_param_when_user_does_not_exist_returns_404() {
        when()
                .get("/" + UUID.randomUUID())
        .then()
                .statusCode(404)
                .body("error", is(true))
                .body("exception", is("UserNotFoundException"));
    }

    @Test
    void get_with_idIn_query_param_when_users_match_returns_200_and_array_of_user_data() {
        UserData alice = createUser("Alice", "alice@gmail.com");
        UserData bob = createUser("Bob", "bob@gmail.com");
        UserData chris = createUser("Chris", "chris@gmail.com");

        Response response = given().param("idIn", alice.id, bob.id).get("/");

        response.then()
                .statusCode(200)
                .body("$", hasSize(2));
        assertThat(response.as(LIST_USER_DATA), containsInAnyOrder(alice, bob));
        assertThat(response.as(LIST_USER_DATA), not(contains(chris)));
    }

    @Test
    void get_with_nameStartsWith_query_param_when_users_match_returns_200_and_array_of_user_data() {
        UserData alice = createUser("Alice", "alice@gmail.com");
        UserData aline = createUser("Aline", "aline@gmail.com");
        UserData alicia = createUser("Alicia", "alicia@gmail.com");
        UserData bob = createUser("Bob", "bob@gmail.com");
        UserData dalila = createUser("Dalila", "dalila@gmail.com");

        Response response = given().param("nameStartsWith", "ali").get("/");

        response.then()
                .statusCode(200)
                .body("$", hasSize(3));
        assertThat(response.as(LIST_USER_DATA), containsInAnyOrder(alice, alicia, aline));
        assertThat(response.as(LIST_USER_DATA), not(containsInAnyOrder(bob, dalila)));
    }

    @Test
    void get_with_emailIn_query_param_when_users_match_returns_200_and_array_of_user_data() {
        UserData alice = createUser("Alice", "alice@gmail.com");
        UserData bob = createUser("Bob", "bob@gmail.com");
        UserData chris = createUser("Chris", "chris@gmail.com");

        Response response = given().param("emailIn", "bob@gmail.com", "chris@gmail.com").get("/");

        response.then()
                .statusCode(200)
                .body("$", hasSize(2));
        assertThat(response.as(LIST_USER_DATA), containsInAnyOrder(bob, chris));
        assertThat(response.as(LIST_USER_DATA), not(containsInAnyOrder(alice)));
    }

    @Test
    void get_with_multiple_query_param_when_users_match_returns_200_and_array_of_user_data() {
        UserData alice = createUser("Alice", "alice@gmail.com");
        UserData bob = createUser("Bob", "bob@gmail.com");
        UserData chris = createUser("Chris", "chris@gmail.com");

        Response response = given()
                .param("nameStartsWith", "Bob")
                .param("emailIn", "bob@gmail.com", "chris@gmail.com").get("/");

        response.then()
                .statusCode(200)
                .body("$", hasSize(1));
        assertThat(response.as(LIST_USER_DATA), containsInAnyOrder(bob));
        assertThat(response.as(LIST_USER_DATA), not(containsInAnyOrder(alice, chris)));
    }

    @Test
    void get_with_query_param_when_users_dont_match_returns_200_and_empty_array() {
        createUser("Alice", "alice@gmail.com");
        createUser("Bob", "bob@gmail.com");
        createUser("Chris", "chris@gmail.com");

        Response response = given().param("nameStartsWith", "John").get("/");

        response.then()
                .statusCode(200)
                .body("$", hasSize(0));
        assertThat(response.as(List.class), is(Collections.emptyList()));
    }

    @Test
    void get_with_no_query_params_returns_200_and_array_of_all_user_data() {
        UserData alice = createUser("Alice", "alice@gmail.com");
        UserData bob = createUser("Bob", "bob@gmail.com");
        UserData chris = createUser("Chris", "chris@gmail.com");

        Response response = get("/");

        response.then()
                .statusCode(200)
                .body("$", hasSize(3));
        assertThat(response.as(LIST_USER_DATA), containsInAnyOrder(bob, alice, chris));
    }

    @Test
    void put_with_incorrect_payload_returns_400() {
        given()
                .body("{\"unknownProperty\": true }")
        .when()
                .put("/{id}", UUID.randomUUID())
        .then()
                .statusCode(400)
                .body("error", is(true))
                .body("exception", is("InvalidDtoException"));
    }

    @Test
    void put_when_user_does_not_exist_returns_404() {
        UpdateUser updateUser = new UpdateUser("test", "email@test.com");
        given()
                .body(gson.toJson(updateUser))
        .when()
                .put("/{id}", UUID.randomUUID())
        .then()
                .statusCode(404)
                .body("error", is(true))
                .body("exception", is("UserNotFoundException"));
    }

    @Test
    void put_when_user_exists_returns_200_and_user_data() {
        UserData alice = createUser("Alice", "alice@gmail.com");
        UpdateUser bob = new UpdateUser("Bob", "bob@gmail.com");
        given()
                .body(gson.toJson(bob))
        .when()
                .put("/{id}", alice.id)
        .then()
                .statusCode(200)
                .body("id", is(alice.id))
                .body("name", is(bob.name))
                .body("email", is(bob.email));
    }

    @Test
    void delete_when_user_exists_returns_204_and_deletes_user() {
        UserData alice = createUser("Alice", "alice@gmail.com");

        when()
                .delete("/{id}", alice.id)
        .then()
                .statusCode(204);

        when()
                .get("/{id}", alice.id)
        .then()
                .statusCode(404);
    }

    @Test
    void delete_when_user_does_not_exist_returns_404() {
        when()
                .delete("/{id}", UUID.randomUUID())
        .then()
                .statusCode(404);
    }

    private UserData createUser(String name, String email) {
        CreateUser createUser = new CreateUser(name, email);
        return given().body(gson.toJson(createUser))
                      .post("/")
                      .andReturn()
                      .as(UserData.class);
    }

}