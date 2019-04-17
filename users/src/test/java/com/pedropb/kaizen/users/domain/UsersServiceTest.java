package com.pedropb.kaizen.users.domain;

import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.domain.exceptions.UserAlreadyCreatedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        usersService = new UsersService(usersRepository);
    }

    @AfterEach
    void tearDown() {
        Mockito.verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void listUsers_calls_usersRepository_findUsers_withNoQuery() {
        usersService.listUsers();

        verify(usersRepository, times(1)).findAllUsers();
    }

    @Test
    void createUser_when_usersRepository_returns_false_throws_exception() {
        CreateUser dto = new CreateUser("John", "john@gmail.com");
        when(usersRepository.save(any(User.class))).thenReturn(false);

        assertThrows(UserAlreadyCreatedException.class, () -> { usersService.createUser(dto); });
        verify(usersRepository, times(1)).save(any(User.class));
    }
}