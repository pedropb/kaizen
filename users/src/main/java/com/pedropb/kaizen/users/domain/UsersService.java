package com.pedropb.kaizen.users.domain;

import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.out.UserCreated;
import com.pedropb.kaizen.users.domain.exceptions.UserAlreadyCreatedException;
import com.pedropb.kaizen.users.domain.models.User;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersService {

    private final UsersRepository usersRepository;

    @Inject
    UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> listUsers() {
        return usersRepository.findAllUsers();
    }

    public UserCreated createUser(CreateUser userDto) {
        User user = User.create(UUID.randomUUID().toString(),
                                userDto.name,
                                userDto.email,
                                Optional.empty());

        if (!usersRepository.save(user)) {
            throw new UserAlreadyCreatedException();
        }

        return new UserCreated(user.id(), user.name(), user.email());
    }
}
