package com.pedropb.kaizen.users.domain;


import com.google.common.collect.Sets;
import com.pedropb.kaizen.users.domain.models.User;
import com.pedropb.kaizen.users.domain.models.UserQuery;
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
    void findAllUsers_returns_all_users() {
        List<User> allUsers = populateUsers();
        List<User> usersFound = repository().findAllUsers();
        assertThat(usersFound, is(allUsers));
    }

    @Test
    void findUsers_with_idIn_query_returns_matching_users() {
        List<User> allUsers = populateUsers();
        Collections.shuffle(allUsers);

        Set<User> expectedUsers = new HashSet<>(allUsers.subList(0, new Random().nextInt(allUsers.size())));
        Set<String> ids = expectedUsers.stream().map(User::id).collect(Collectors.toSet());
        UserQuery query = UserQuery.builder().idIn(ids).build();

        Set<User> usersFound = new HashSet<>(repository().findUsers(query));
        assertThat(usersFound, is(expectedUsers));
    }

    @Test
    void findUsers_with_emailIn_query_returns_matching_users() {
        List<User> allUsers = populateUsers();
        Collections.shuffle(allUsers);

        Set<User> expectedUsers = new HashSet<>(allUsers.subList(0, new Random().nextInt(allUsers.size())));
        Set<String> emails = expectedUsers.stream().map(User::email).collect(Collectors.toSet());
        UserQuery query = UserQuery.builder().emailIn(emails).build();

        Set<User> usersFound = new HashSet<>(repository().findUsers(query));
        assertThat(usersFound, is(expectedUsers));
    }

    @Test
    void findUsers_with_name_query_returns_matching_users() {
        User alice = User.testBuilder().name("Alice").build();
        User alicia = User.testBuilder().name("Alicia").build();
        User bob = User.testBuilder().name("Bob").build();
        User dalila = User.testBuilder().name("Dalila").build();

        repository().save(alice, alicia, bob, dalila);
        UserQuery query = UserQuery.builder().nameStartsWith("ali").build();

        Set<User> usersFound = new HashSet<>(repository().findUsers(query));
        assertThat(usersFound, is(Sets.newHashSet(alice, alicia)));
    }

    @Test
    void save_one_user_when_user_does_not_exist_then_insert_and_return_true() {
        User user = User.testBuilder().build();
        assert !repository().findUserById(user.id()).isPresent();

        assertThat(repository().save(user), is(true));

        Optional<User> savedUser = repository().findUserById(user.id());
        assertThat(savedUser.isPresent(), is(true));
        assertThat(savedUser.get(), is(user));
    }

    @Test
    void save_one_user_when_user_exists_and_version_matches_then_update_and_increment_version_and_return_true() {
        User user = User.testBuilder().build();
        assert repository().save(user);

        User insertedUser = repository().findUserById(user.id()).orElseThrow(IllegalStateException::new);
        User updatedUser = insertedUser.toBuilder().name("Update test").build();
        assertThat(repository().save(updatedUser), is(true));
        assertThat(repository().findUserById(user.id()).orElseThrow(IllegalStateException::new),
                   is(updatedUser));
    }
    @Test
    void save_user_when_user_exists_and_version_mismatches_then_does_not_update_and_return_false() {
        User user = User.testBuilder().name("ailce").build();
        assert repository().save(user);

        User savedUser = repository().findUserById(user.id()).orElseThrow(IllegalStateException::new);
        User concurrentChange = savedUser.toBuilder().name("alice").build();
        User intendedChange = savedUser.toBuilder().name("Alice").build();

        assert repository().save(concurrentChange);
        assertThat(repository().save(intendedChange), is(false));
        User updatedUser = repository().findUserById(user.id()).orElseThrow(IllegalStateException::new);
        assertThat(updatedUser.version(), is(intendedChange.version().map(v -> v + 1)));
    }

    @Test
    void save_multiple_users_when_users_exist_and_version_matches_then_update_and_increment_version_and_return_true() {
        User user1 = User.testBuilder().build();
        User user2 = User.testBuilder().build();
        assert Arrays.stream(repository().save(user1, user2)).allMatch(i -> i == 1);

        UserQuery query = UserQuery.builder().idIn(Sets.newHashSet(user1.id(), user2.id())).build();
        List<User> savedUsers = repository().findUsers(query);
        List<User> updatedUsers = savedUsers.stream()
                                            .map(user -> user.toBuilder().name(user.name() + " Updated").build())
                                            .collect(Collectors.toList());

        assertThat(Arrays.stream(repository().save(updatedUsers)).allMatch(i -> i == 1), is(true));
        assertThat(repository().findUsers(query), is(updatedUsers));
    }

    @Test
    void delete_multiple_users_when_users_exist_and_version_matches_then_delete_and_return_ones() {
        populateUsers();
        List<User> users = repository().findAllUsers();

        assertThat(Arrays.stream(repository().delete(users)).allMatch(i -> i==1), is(true));
        assertThat(repository().findAllUsers(), is(Collections.emptyList()));
    }

    @Test
    void delete_users_when_users_dont_exist_then_return_zeros() {
        List<User> users = Arrays.asList(User.testBuilder().build(), User.testBuilder().build(), User.testBuilder().build());
        assertThat(Arrays.stream(repository().delete(users)).allMatch(i -> i==0), is(true));
    }

    @Test
    void delete_users_when_users_version_dont_match_then_return_zeros() {
        populateUsers();
        List<User> users = repository().findAllUsers();
        List<User> updatedUsers = users.stream()
                                       .map(user -> user.toBuilder().name(user.name() + " updated").build())
                                       .collect(Collectors.toList());
        assert Arrays.stream(repository().save(updatedUsers)).allMatch(i -> i == 1);

        assertThat(Arrays.stream(repository().delete(users)).allMatch(i -> i==0), is(true));
    }

    private List<User> populateUsers() {
        List<User> users = IntStream.range(0, 1 + new Random().nextInt(20))
                                    .mapToObj(i -> User.testBuilder().build())
                                    .collect(Collectors.toList());
        repository().save(users);
        return users;
    }
}