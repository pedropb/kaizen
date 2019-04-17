package com.pedropb.kaizen.users.domain;

import com.google.common.collect.ImmutableSet;
import com.pedropb.kaizen.users.domain.models.User;
import com.pedropb.kaizen.users.domain.models.UserQuery;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface UsersRepository {

    default List<User> findAllUsers() {
        return findUsers(UserQuery.builder().build());
    }

    default Optional<User> findUserById(String userId) {
        UserQuery query = UserQuery.builder().idIn(ImmutableSet.of(userId)).build();
        return findUsers(query).stream().findFirst();
    }

    List<User> findUsers(UserQuery userQuery);

    int[] save(Collection<User> users);

    default int[] save(User... users) {
        return save(Arrays.asList(users));
    }

    default boolean save(User user) {
        return save(Collections.singletonList(user))[0] == 1;
    }


    default boolean deleteById(String userId) {
        return findUserById(userId).filter(user -> delete(Collections.singletonList(user))[0] == 1).isPresent();

    }

    int[] delete(Collection<User> singletonList);
}
