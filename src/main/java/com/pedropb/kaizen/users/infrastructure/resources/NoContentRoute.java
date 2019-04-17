package com.pedropb.kaizen.users.infrastructure.resources;

import spark.Request;

@FunctionalInterface
public interface NoContentRoute {
    void handle(Request req);
}
