package com.pedropb.kaizen.users.domain;

import com.google.common.collect.Sets;
import com.pedropb.kaizen.users.api.in.CreateUser;
import com.pedropb.kaizen.users.api.out.UserData;
import com.pedropb.kaizen.users.domain.exceptions.UserAlreadyCreatedException;
import com.pedropb.kaizen.users.domain.exceptions.UserNotFoundException;
import com.pedropb.kaizen.users.domain.models.User;
import com.pedropb.kaizen.users.domain.models.UserQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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

    @Captor
    private ArgumentCaptor<UserQuery> userQueryCaptor;

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

    @Test
    void findUserById_when_usersRepository_returns_empty_then_throw_exception() {
        String id = UUID.randomUUID().toString();
        when(usersRepository.findUserById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> usersService.findUserById(id));
        verify(usersRepository, times(1)).findUserById(id);
    }

    @Test
    void findUserById_when_usersRepository_returns_user_then_return_userData() {
        String id = UUID.randomUUID().toString();
        User user = User.testBuilder().id(id).build();
        when(usersRepository.findUserById(id)).thenReturn(Optional.of(user));

        assertThat(usersService.findUserById(id), is(new UserData(user.id(), user.name(), user.email())));
        verify(usersRepository, times(1)).findUserById(id);
    }

    @Test
    void findUsers_calls_usersRepository_with_correct_query() {
        String[] ids = new String[] { "a", "b", "c" };
        String name = "John";
        String[] emails = null;
        UserQuery expectedQuery = UserQuery.builder()
                                           .idIn(Sets.newHashSet("a", "b", "c"))
                                           .nameStartsWith("John")
                                           .build();

        usersService.findUsers(ids, name, emails);
        verify(usersRepository, times(1)).findUsers(userQueryCaptor.capture());
        UserQuery query = userQueryCaptor.getValue();
        assertThat(query, is(expectedQuery));
    }

    @Test
    void findUsers_when_usersRepository_returns_empty_list_returns_empty_list() {
        when(usersRepository.findUsers(any(UserQuery.class))).thenReturn(Collections.emptyList());

        assertThat(usersService.findUsers(null, null, null), is(Collections.emptyList()));
        verify(usersRepository, times(1)).findUsers(any(UserQuery.class));
    }

    @Test
    void findUsers_when_usersRepository_returns_user_list_returns_user_data_list() {
        User user1 = User.testBuilder().build();
        User user2 = User.testBuilder().build();
        User user3 = User.testBuilder().build();
        when(usersRepository.findUsers(any(UserQuery.class))).thenReturn(Arrays.asList(user1, user2, user3));

        UserData userData1 = new UserData(user1.id(), user1.name(), user1.email());
        UserData userData2 = new UserData(user2.id(), user2.name(), user2.email());
        UserData userData3 = new UserData(user3.id(), user3.name(), user3.email());
        assertThat(usersService.findUsers(null, null, null), containsInAnyOrder(userData1, userData2, userData3));
        verify(usersRepository, times(1)).findUsers(any(UserQuery.class));
    }
}