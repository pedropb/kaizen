package com.pedropb.kaizen.users.domain;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public abstract class UsersRepositoryTest {
    public abstract UsersRepository repository();

    @Test
    void findUsers_returns_all_users() {
        User alice = User.create(UUID.randomUUID().toString(),
                                 "Alice",
                                 "alice@gmail.com");

        User bob = User.create(UUID.randomUUID().toString(),
                               "Bob",
                               "bob@gmail.com");

        User chris = User.create(UUID.randomUUID().toString(),
                                 "Chris",
                                 "chris@gmail.com");


        List<User> allUsers = Arrays.asList(alice, bob, chris);
        repository().save(allUsers);

        List<User> usersFound = repository().findUsers();
        assertThat(usersFound, is(allUsers));

    }
}