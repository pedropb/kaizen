package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.Inject;
import com.pedropb.kaizen.users.domain.User;
import com.pedropb.kaizen.users.domain.UserQuery;
import com.pedropb.kaizen.users.domain.UsersRepository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JooqUsersRepository implements UsersRepository {

    @Inject
    public JooqUsersRepository(DataSource dataSource) {

    }

    @Override
    public List<User> findUsers(UserQuery query) {
        return Collections.emptyList();
    }

    @Override
    public void save(Collection<User> users) {

    }
}
