package com.pedropb.kaizen.users.infrastructure;

import com.pedropb.kaizen.users.domain.UsersService;

import javax.inject.Inject;
import com.google.gson.Gson;

import static spark.Spark.get;

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
        get("/", json((req, res) -> usersService.listUsers()), gson::toJson);
    }
}
