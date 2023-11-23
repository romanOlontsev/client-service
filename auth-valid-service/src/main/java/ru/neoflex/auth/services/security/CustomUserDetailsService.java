package ru.neoflex.auth.services.security;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService {
    UserDetailsService getUserDetailsService();
}
