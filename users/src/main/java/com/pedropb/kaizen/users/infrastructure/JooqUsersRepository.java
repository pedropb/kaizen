package com.pedropb.kaizen.users.infrastructure;

import com.google.inject.Inject;
import com.pedropb.kaizen.users.domain.User;
import com.pedropb.kaizen.users.domain.UserQuery;
import com.pedropb.kaizen.users.domain.UsersRepository;
import infrastructure.generated.jooq.tables.records.UsersRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.Query;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static infrastructure.generated.jooq.Tables.USERS;
import static org.jooq.impl.DSL.noCondition;


public class JooqUsersRepository implements UsersRepository {

    DataSource dataSource;

    @Inject
    public JooqUsersRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findUsers(UserQuery query) {
        DSLContext context = DSL.using(dataSource, SQLDialect.H2);
        return context.selectFrom(USERS)
                      .where(queryToCondition(query))
                      .fetch()
                      .stream()
                      .map(JooqUsersRepository::deserialize)
                      .collect(Collectors.toList());
    }

    @Override
    public boolean save(Collection<User> users) {
        DSLContext context = DSL.using(dataSource, SQLDialect.H2);

        List<Query> upsertQueries = users.stream()
                                         .map(JooqUsersRepository::serialize)
                                         .map(record -> context.insertInto(USERS).set(record).onDuplicateKeyUpdate().set(record))
                                         .collect(Collectors.toList());

        int[] result = context.batch(upsertQueries).execute();

        return IntStream.of(result).anyMatch(i -> i == 0);
    }

    private static UsersRecord serialize(User user) {
        return new UsersRecord(
                user.id(),
                user.name(),
                user.email()
        );
    }

    private static Condition[] queryToCondition(UserQuery query) {
        return new Condition[] {
            query.ids().map(USERS.ID::in).orElse(noCondition()),
            query.names()
                 .map(set -> set.stream().map(String::toLowerCase).collect(Collectors.toSet()))
                 .map(USERS.NAME::in).orElse(noCondition()),
            query.email().map(USERS.EMAIL::like).map(Condition.class::cast).orElse(noCondition())
        };
    }

    private static User deserialize(UsersRecord usersRecord) {
        return User.create(
                usersRecord.getId(),
                usersRecord.getName(),
                usersRecord.getEmail()
        );
    }

}
