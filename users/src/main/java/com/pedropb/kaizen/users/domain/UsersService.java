package com.pedropb.kaizen.users.domain;

import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.out.UserData;
import com.pedropb.kaizen.users.domain.exceptions.UserAlreadyCreatedException;
import com.pedropb.kaizen.users.domain.models.User;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UsersService {

    private final UsersRepository usersRepository;

    @Inject
    UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<UserData> listUsers() {
        return usersRepository.findAllUsers()
                              .stream()
                              .map(user -> new UserData(user.id(), user.name(), user.email()))
                              .collect(Collectors.toList());
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
}
