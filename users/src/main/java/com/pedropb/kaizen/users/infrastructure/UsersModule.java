package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.AbstractModule;
import com.pedropb.kaizen.users.domain.UsersService;

public class UsersModule extends AbstractModule {

    private final UsersService usersService;
    private final UsersResources usersResources;

    public UsersModule() {
        this.usersService = new UsersService();
        this.usersResources = new UsersResources(usersService);
    }

    @Override
    protected void configure() {
        bind(UsersService.class).toInstance(usersService);
        bind(UsersResources.class).toInstance(usersResources);
    }
}
