package com.pedropb.kaizen.users.infrastructure.modules;

import com.google.inject.AbstractModule;
import com.pedropb.kaizen.users.domain.UsersRepository;
import com.pedropb.kaizen.users.domain.UsersService;
import com.pedropb.kaizen.users.infrastructure.persistence.JooqUsersRepository;
import com.pedropb.kaizen.users.infrastructure.resources.UsersResources;

public class UsersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JooqUsersRepository.class);
        bind(UsersService.class);
        bind(UsersResources.class);

        bind(UsersRepository.class).to(JooqUsersRepository.class);
    }
}
