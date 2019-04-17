package com.pedropb.kaizen.users.infrastructure;

import com.google.gson.JsonSyntaxException;
import com.pedropb.kaizen.users.api.exceptions.InvalidDtoException;
import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.out.UserCreated;
import com.pedropb.kaizen.users.domain.UsersService;

import javax.inject.Inject;
import com.google.gson.Gson;
import com.pedropb.kaizen.users.domain.exceptions.UserAlreadyCreatedException;
import spark.Request;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

public class UsersResources implements Resource {

    private final UsersService usersService;
    private final Gson gson;

    @Inject
    public UsersResources(UsersService usersService) {
        this.usersService = usersService;
        this.gson = new Gson();
    }

    @Override
    public void configure() {
        post("/", json((req, res) -> createUser(req)), gson::toJson);

        get("/", json((req, res) -> usersService.listUsers()), gson::toJson);

        //get with query parameter
        //get with path parameter
        //put
        //delete

        exception(UserAlreadyCreatedException.class, exceptionJson(409));
        exception(InvalidDtoException.class, exceptionJson(400));
        exception(RuntimeException.class, exceptionJson(500));
    }

    private UserCreated createUser(Request req) {
        CreateUser userDto;
        try {
            userDto = gson.fromJson(req.body(), CreateUser.class);
        }
        catch (JsonSyntaxException ex) {
            throw new InvalidDtoException();
        }

        return usersService.createUser(userDto);
    }
}
