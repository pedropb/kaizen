package com.pedropb.kaizen.users.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
}