package com.pedropb.kaizen.users.infrastructure;

import com.pedropb.kaizen.users.domain.User;
import com.pedropb.kaizen.users.domain.UsersRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class JooqUsersRepository implements UsersRepository {
    @Override
    public List<User> findUsers() {
        throw new NotImplementedException();
    }
}
