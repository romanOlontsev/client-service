package ru.neoflex.auth.services.security.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.neoflex.auth.repository.UserRepository;
import ru.neoflex.auth.services.security.CustomUserDetailsService;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetailsService getUserDetailsService() {
        return username -> repository.findUserByLogin(username)
                                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
