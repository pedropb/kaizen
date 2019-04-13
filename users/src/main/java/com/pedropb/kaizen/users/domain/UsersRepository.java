package com.pedropb.kaizen.users.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface UsersRepository {

    default List<User> findAllUsers() {
        return findUsers(UserQuery.builder().build());
    }

    default Optional<User> findUserById(String userId) {
        UserQuery query = UserQuery.builder().idIn(Optional.of(Collections.singleton(userId))).build();
        return findUsers(query).stream().findFirst();
    }

    List<User> findUsers(UserQuery userQuery);

    int[] save(Collection<User> users);

    default boolean save(User user) {
        return save(Collections.singletonList(user))[0] == 1;
    }
}
