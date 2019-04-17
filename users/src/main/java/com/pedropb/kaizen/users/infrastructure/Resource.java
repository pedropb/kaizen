package com.pedropb.kaizen.users.infrastructure;

import spark.ExceptionHandler;
import spark.Route;

public interface Resource {
    void configure();

    default Route json(Route route) {
        return (req, res) -> {
          res.type("application/json");
          return route.handle(req, res);
        };
    }

    default ExceptionHandler exceptionJson(Integer statusCode) {
        return (e, req, res) -> {
            res.type("application/json");
            res.status(statusCode);
            res.body("{ \"error\": true, \"exception\": \"" + e.getClass().getSimpleName() + "\"}");
            e.printStackTrace();
            System.err.println(e.getMessage());
        };

    }
}
