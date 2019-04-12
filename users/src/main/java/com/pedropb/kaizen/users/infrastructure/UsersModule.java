package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.AbstractModule;
import com.pedropb.kaizen.users.domain.UsersRepository;
import com.pedropb.kaizen.users.domain.UsersService;

public class UsersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JooqUsersRepository.class);
        bind(UsersService.class);
        bind(UsersResources.class);

        bind(UsersRepository.class).to(JooqUsersRepository.class);
//        bind(DataSource.class).toInstance();
    }
}
