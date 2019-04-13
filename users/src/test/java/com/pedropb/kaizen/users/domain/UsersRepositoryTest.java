package com.pedropb.kaizen.users.domain;


import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public abstract class UsersRepositoryTest {
    public abstract UsersRepository repository();

    @Test
    void findUsers_returns_all_users() {
        List<User> allUsers = populateUsers();
        List<User> usersFound = repository().findUsers();
        assertThat(usersFound, is(allUsers));
    }

    @Test
    void findUsers_with_idIn_query_returns_matching_users() {
        List<User> allUsers = populateUsers();
        Collections.shuffle(allUsers);

        Set<User> expectedUsers = new HashSet<>(allUsers.subList(0, new Random().nextInt(allUsers.size())));
        Set<String> ids = expectedUsers.stream().map(User::id).collect(Collectors.toSet());
        UserQuery query = UserQuery.builder().idIn(Optional.of(ids)).build();

        Set<User> usersFound = new HashSet<>(repository().findUsers(query));
        assertThat(usersFound, is(expectedUsers));
    }

    @Test
    void findUsers_with_emailIn_query_returns_matching_users() {
        List<User> allUsers = populateUsers();
        Collections.shuffle(allUsers);

        Set<User> expectedUsers = new HashSet<>(allUsers.subList(0, new Random().nextInt(allUsers.size())));
        Set<String> emails = expectedUsers.stream().map(User::email).collect(Collectors.toSet());
        UserQuery query = UserQuery.builder().emailIn(Optional.of(emails)).build();

        Set<User> usersFound = new HashSet<>(repository().findUsers(query));
        assertThat(usersFound, is(expectedUsers));
    }

    @Test
    void findUsers_with_name_query_returns_matching_users() {
        User alice = User.testBuilder().name("Alice").build();
        User alicia = User.testBuilder().name("Alicia").build();
        User bob = User.testBuilder().name("Bob").build();
        User dalila = User.testBuilder().name("Dalila").build();

        populateUsers(alice, alicia, bob, dalila);
        UserQuery query = UserQuery.builder().nameStartWith(Optional.of("ali")).build();

        Set<User> usersFound = new HashSet<>(repository().findUsers(query));
        assertThat(usersFound, is(Sets.newHashSet(alice, alicia)));
    }

    private List<User> populateUsers() {
        List<User> users = IntStream.range(0, 1 + new Random().nextInt(20))
                                    .boxed()
                                    .map(i -> User.testBuilder().build())
                                    .collect(Collectors.toList());
        repository().save(users);
        return users;
    }

    private List<User> populateUsers(User... users) {
        List<User> userList = Arrays.asList(users);
        repository().save(userList);
        return userList;
    }
}