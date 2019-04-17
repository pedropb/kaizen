package com.pedropb.kaizen.users.infrastructure.resources;

import spark.Request;

@FunctionalInterface
public interface DefaultRoute {
    Object handle(Request req);
}
