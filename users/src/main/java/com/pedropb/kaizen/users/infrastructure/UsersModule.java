package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.AbstractModule;
import com.pedropb.kaizen.users.domain.UsersRepository;
import com.pedropb.kaizen.users.domain.UsersService;

public class UsersModule extends AbstractModule {

    private final UsersService usersService;
    private final UsersResources usersResources;
    private final JooqUsersRepository jooqUsersRepository;

    public UsersModule() {
        this.jooqUsersRepository = new JooqUsersRepository();
        this.usersService = new UsersService(jooqUsersRepository);
        this.usersResources = new UsersResources(usersService);
    }

    @Override
    protected void configure() {
        bind(JooqUsersRepository.class).toInstance(jooqUsersRepository);
        bind(UsersService.class).toInstance(usersService);
        bind(UsersResources.class).toInstance(usersResources);

        bind(UsersRepository.class).to(JooqUsersRepository.class);
    }
}
