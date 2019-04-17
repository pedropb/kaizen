package com.pedropb.kaizen.users.infrastructure.persistence;

import com.google.inject.Inject;
import com.pedropb.kaizen.users.domain.models.User;
import com.pedropb.kaizen.users.domain.models.UserQuery;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static infrastructure.generated.jooq.Tables.USERS;
import static org.jooq.impl.DSL.noCondition;


public class JooqUsersRepository implements UsersRepository {

    private DataSource dataSource;

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
    public int[] save(Collection<User> users) {
        DSLContext context = DSL.using(dataSource, SQLDialect.H2);

        Stream<Query> insertQueries = users.stream()
                                           .filter(user -> !user.version().isPresent())
                                           .map(user -> context.insertInto(USERS, USERS.ID, USERS.NAME, USERS.EMAIL)
                                                             .values(user.id(), user.name(), user.email()));

        Stream<Query> updateQueries = users.stream()
                                           .filter(user -> user.version().isPresent())
                                           .map(user -> context.update(USERS)
                                                               .set(newVersion(user))
                                                               .where(USERS.ID.eq(user.id()),
                                                                      USERS.VERSION.eq(user.version().get())));

        return context.batch(Stream.concat(insertQueries, updateQueries).collect(Collectors.toList())).execute();
    }

    private static UsersRecord newVersion(User user) {
        return new UsersRecord(
                user.id(),
                user.name(),
                user.email(),
                user.version().map(version -> version + 1).orElse(0)
        );
    }

    private static Condition[] queryToCondition(UserQuery query) {
        return new Condition[] {
            query.idIn() != null ? USERS.ID.in(query.idIn()) : noCondition(),
            query.nameStartWith() != null ? USERS.NAME.likeIgnoreCase(query.nameStartWith().concat("%")) : noCondition(),
            query.emailIn() != null
                    ? USERS.EMAIL.lower().in(query.emailIn().stream().map(String::toLowerCase).collect(Collectors.toSet()))
                    : noCondition()
        };
    }

    private static User deserialize(UsersRecord usersRecord) {
        return User.create(
                usersRecord.getId(),
                usersRecord.getName(),
                usersRecord.getEmail(),
                Optional.of(usersRecord.getVersion())
        );
    }

}
