package com.pedropb.kaizen.users.domain;

import javax.inject.Inject;
import java.util.List;

public class UsersService {

    private final UsersRepository usersRepository;

    @Inject
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> listUsers() {
        return usersRepository.findAllUsers();
    }
}
