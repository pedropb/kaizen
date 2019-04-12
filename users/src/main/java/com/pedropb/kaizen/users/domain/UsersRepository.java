package com.pedropb.kaizen.users.domain;

import java.util.List;

public interface UsersRepository {
    List<User> findUsers();
}
