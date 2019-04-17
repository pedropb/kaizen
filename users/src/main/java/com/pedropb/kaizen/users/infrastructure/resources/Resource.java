package com.pedropb.kaizen.users.infrastructure.resources;

import com.google.common.base.Throwables;
import spark.ExceptionHandler;
import spark.Route;

import java.util.logging.Level;
import java.util.logging.Logger;

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
            Logger.getAnonymousLogger().log(Level.SEVERE, res.body());
            Logger.getAnonymousLogger().log(Level.SEVERE, Throwables.getStackTraceAsString(e));
        };

    }
}
