package ru.neoflex.auth.services.security.impls;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceImplTest {

    @InjectMocks
    private CustomUserDetailsServiceImpl customUserDetailsService;
    @Mock
    private UserRepository repository;

    @Test
    void getUserDetailsService_shouldReturnUserDetailsService() {
        String username = "test";
        Optional<User> userOptional = Optional.of(User.builder()
                                                      .id(-1L)
                                                      .login(username)
                                                      .password(username)
                                                      .build());

        when(repository.findUserByLogin(username)).thenReturn(userOptional);

        UserDetailsService userDetailsService = customUserDetailsService.getUserDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertAll(
                () -> assertThat(userDetailsService).isNotNull(),
                () -> assertThat(userDetails).isNotNull()
                                             .extracting(UserDetails::getUsername)
                                             .isEqualTo(username),
                () -> assertThat(userDetails).extracting(UserDetails::getPassword)
                                             .isEqualTo(username)
                 );
    }

    @Test
    void getUserDetailsService_shouldThrowUsernameNotFoundException() {
        String username = "test";

        UserDetailsService userDetailsService = customUserDetailsService.getUserDetailsService();

        assertAll(
                () -> assertThatThrownBy(
                        () -> userDetailsService.loadUserByUsername(username)).isInstanceOf(UsernameNotFoundException.class)
                                                                              .hasMessage("User not found")

                 );
    }
}