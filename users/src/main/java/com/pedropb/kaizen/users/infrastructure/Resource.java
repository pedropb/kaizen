package com.pedropb.kaizen.users.infrastructure;

import spark.Route;

public interface Resource {
    void configure();

    default Route json(Route route) {
        return (req, res) -> {
          res.type("application/json");
          return route.handle(req, res);
        };
    }
}
