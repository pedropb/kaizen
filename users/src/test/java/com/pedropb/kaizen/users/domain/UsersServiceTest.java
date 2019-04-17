package com.pedropb.kaizen.users.domain;

import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.out.UserData;
import com.pedropb.kaizen.users.domain.exceptions.UserAlreadyCreatedException;
import com.pedropb.kaizen.users.domain.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;
    private UsersService usersService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

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

    @Test
    void createUser_when_usersRepository_returns_true_then_returns_user_created() {
        CreateUser dto = new CreateUser("John", "john@gmail.com");
        when(usersRepository.save(any(User.class))).thenReturn(true);

        UserData returnedUser = usersService.createUser(dto);
        verify(usersRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        UserData expectedUser = new UserData(savedUser.id(), savedUser.name(), savedUser.email());

        assertThat(returnedUser, is(expectedUser));
    }
}