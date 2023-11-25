package ru.neoflex.auth.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsResponse {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "is_account_non_expired")
    private boolean isAccountNonExpired;

    @JsonProperty(value = "is_account_non_locked")
    private boolean isAccountNonLocked;

    @JsonProperty(value = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @JsonProperty(value = "is_enabled")
    private boolean isEnabled;

    @JsonProperty(value = "authorities")
    Collection<? extends GrantedAuthority> authorities;
}
