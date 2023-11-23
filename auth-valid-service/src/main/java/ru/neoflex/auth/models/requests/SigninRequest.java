package ru.neoflex.auth.models.requests;

import jakarta.persistence.JoinColumn;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SigninRequest {

    @JoinColumn(name = "login")
    private String login;

    @JoinColumn(name = "password")
    private String password;
}