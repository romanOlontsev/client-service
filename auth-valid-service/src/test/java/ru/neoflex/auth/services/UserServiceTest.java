package ru.neoflex.auth.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.neoflex.auth.exceptions.DataAlreadyExistsException;
import ru.neoflex.auth.exceptions.DataNotFoundException;
import ru.neoflex.auth.mappers.UserMapper;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Spy
    private UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        User user = User.builder()
                        .id(-1L)
                        .login("test")
                        .password("test")
                        .build();

        when(repository.findUserByLogin(any())).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername(user.getUsername());

        assertAll(
                () -> assertThat(userDetails).isNotNull()
                                             .extracting(UserDetails::getUsername)
                                             .isEqualTo(user.getUsername()),
                () -> assertThat(userDetails).extracting(UserDetails::getPassword)
                                             .isEqualTo(user.getPassword())
                 );
    }

    @Test
    void loadUserByUsername_shouldThrowDataNotFoundException() {
        when(repository.findUserByLogin(any())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(
                        () -> service.loadUserByUsername("test")).isInstanceOf(DataNotFoundException.class)
                                                                 .hasMessage("User with login test is not found")
                 );
    }

    @Test
    void addUser_shouldReturnUser() {
        SignupRequest request = SignupRequest.builder()
                                             .login("test")
                                             .password("$2a$12$z3Xw2yKqS2kzF8rptYeBYuGn31eBruleF8c4qu9BRPnwWALiis3NC")
                                             .build();
        User user = mapper.userFromSignupRequest(request);

        when(repository.findUserByLogin(any())).thenReturn(Optional.empty());
        when(repository.persist(any())).thenReturn(user);

        User response = service.addUser(request);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .extracting(User::getUsername)
                                          .isEqualTo(user.getUsername())
                 );
    }

    @Test
    void addUser_shouldThrowDataAlreadyExistsException() {
        SignupRequest request = SignupRequest.builder()
                                             .login("test")
                                             .password("$2a$12$z3Xw2yKqS2kzF8rptYeBYuGn31eBruleF8c4qu9BRPnwWALiis3NC")
                                             .build();
        User user = mapper.userFromSignupRequest(request);

        when(repository.findUserByLogin(any())).thenReturn(Optional.of(user));

        assertAll(
                () -> assertThatThrownBy(() -> service.addUser(request)).isInstanceOf(DataAlreadyExistsException.class)
                                                                        .hasMessage("User with login test already exists")
                 );
    }
}