package com.pedropb.kaizen.users.infrastructure.resources;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.pedropb.kaizen.users.api.exceptions.InvalidDtoException;
import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.in.UpdateUser;
import com.pedropb.kaizen.users.api.out.UserData;
import com.pedropb.kaizen.users.domain.UsersService;

import javax.inject.Inject;
import com.google.gson.Gson;
import com.pedropb.kaizen.users.domain.exceptions.UserAlreadyCreatedException;
import com.pedropb.kaizen.users.domain.exceptions.UserChangedBeforeUpdateException;
import com.pedropb.kaizen.users.domain.exceptions.UserNotFoundException;
import spark.Request;

import java.util.List;
import java.util.Objects;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

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
        post("/", json(this::createUser), gson::toJson);

        get("/:id", json((req) -> usersService.findUserById(req.params("id"))), gson::toJson);

        get("/", json(this::findUsers), gson::toJson);

        put("/:id", json(this::updateUser), gson::toJson);

        delete("/:id", noContent((req) -> usersService.deleteUserById(req.params("id"))), gson::toJson);

        exception(UserAlreadyCreatedException.class, exceptionJson(409));
        exception(UserChangedBeforeUpdateException.class, exceptionJson(409));
        exception(UserNotFoundException.class, exceptionJson(404));
        exception(InvalidDtoException.class, exceptionJson(400));
        exception(RuntimeException.class, exceptionJson(500));
    }

    private UserData updateUser(Request req) {
        UpdateUser userDto;
        try {
            userDto = gson.fromJson(req.body(), UpdateUser.class);
            Objects.requireNonNull(userDto.name);
            Objects.requireNonNull(userDto.email);
        }
        catch (RuntimeException ex) {
            throw new InvalidDtoException();
        }

        return usersService.updateUser(req.params("id"), userDto);
    }

    private List<UserData> findUsers(Request req) {
        String[] idIn = req.queryParamsValues("idIn");
        String nameStartsWith = req.queryParams("nameStartsWith");
        String[] emailIn = req.queryParamsValues("emailIn");

        return usersService.findUsers(idIn, nameStartsWith, emailIn);
    }

    private UserData createUser(Request req) {
        CreateUser userDto;
        try {
            userDto = gson.fromJson(req.body(), CreateUser.class);
            Objects.requireNonNull(userDto.name);
            Objects.requireNonNull(userDto.email);
        }
        catch (RuntimeException ex) {
            throw new InvalidDtoException();
        }

        return usersService.createUser(userDto);
    }
}
