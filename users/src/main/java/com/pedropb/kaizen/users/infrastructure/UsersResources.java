package com.pedropb.kaizen.users.infrastructure;

import static spark.Spark.get;

public class UsersResources implements Resource {

    @Override
    public void configure() {
        get("/hello", (req, res) -> "Hello World");
    }
}
