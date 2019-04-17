package com.pedropb.kaizen.users.domain;

import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.out.UserData;
import com.pedropb.kaizen.users.domain.exceptions.UserAlreadyCreatedException;
import com.pedropb.kaizen.users.domain.exceptions.UserNotFoundException;
import com.pedropb.kaizen.users.domain.models.User;
import com.pedropb.kaizen.users.domain.models.UserQuery;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsersService {

    private final UsersRepository usersRepository;

    @Inject
    UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserData createUser(CreateUser userDto) {
        User user = User.create(UUID.randomUUID().toString(),
                                userDto.name,
                                userDto.email,
                                Optional.empty());

        if (!usersRepository.save(user)) {
            throw new UserAlreadyCreatedException();
        }

        return new UserData(user.id(), user.name(), user.email());
    }

    public UserData findUserById(String id) {
        User user = usersRepository.findUserById(id).orElseThrow(UserNotFoundException::new);
        return new UserData(user.id(), user.name(), user.email());
    }

    public List<UserData> findUsers(String[] idIn, String nameStartsWith, String[] emailIn) {
        UserQuery query = UserQuery.builder()
                                   .idIn(idIn == null ? null : Stream.of(idIn).collect(Collectors.toSet()))
                                   .nameStartsWith(nameStartsWith)
                                   .emailIn(emailIn == null ? null : Stream.of(emailIn).collect(Collectors.toSet()))
                                   .build();

        return usersRepository.findUsers(query)
                              .stream()
                              .map(user -> new UserData(user.id(), user.name(), user.email()))
                              .collect(Collectors.toList());
    }
}
