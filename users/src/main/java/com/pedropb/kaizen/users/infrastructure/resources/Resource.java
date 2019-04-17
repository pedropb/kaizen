package com.pedropb.kaizen.users.infrastructure.resources;

import com.google.common.base.Throwables;
import spark.ExceptionHandler;
import spark.Route;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface Resource {
    void configure();

    default Route json(DefaultRoute route) {
        return (req, res) -> {
          res.type("application/json");
          return route.handle(req);
        };
    }

    default Route noContent(NoContentRoute route) {
        return (req, res) -> {
            res.status(204);
            route.handle(req);
            return "";
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
