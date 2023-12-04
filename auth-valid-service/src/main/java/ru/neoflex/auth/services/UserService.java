package ru.neoflex.auth.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.neoflex.auth.exceptions.DataAlreadyExistsException;
import ru.neoflex.auth.exceptions.DataNotFoundException;
import ru.neoflex.auth.mappers.UserMapper;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = getUserByLogin(username);
        return new org.springframework.security.core.userdetails.User(
                foundUser.getLogin(),
                foundUser.getPassword(),
                foundUser.isEnabled(),
                foundUser.isAccountNonExpired(),
                foundUser.isCredentialsNonExpired(),
                foundUser.isAccountNonLocked(),
                foundUser.getAuthorities());
    }

    public User getUserByLogin(String login) {
        return repository.findUserByLogin(login)
                         .orElseThrow(() -> new DataNotFoundException(
                                 String.format("User with login %s is not found", login)));
    }

    @Transactional
    public User addUser(SignupRequest request) {
        String login = request.getLogin();
        repository.findUserByLogin(login)
                  .ifPresent(user -> {
                      throw new DataAlreadyExistsException("User with login " + login + " already exists");
                  });
        User userToSave = mapper.userFromSignupRequest(request);
        userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
        return repository.persist(userToSave);
    }
}
