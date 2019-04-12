package com.pedropb.kaizen.users.domain;

import java.util.Collection;
import java.util.List;

public interface UsersRepository {
    default List<User> findUsers() {
        return findUsers(UserQuery.builder().build());
    }

    List<User> findUsers(UserQuery userQuery);

    void save(Collection<User> users);
}
