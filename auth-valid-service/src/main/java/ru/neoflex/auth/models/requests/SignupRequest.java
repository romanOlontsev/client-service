package ru.neoflex.auth.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {

    @JsonProperty(value = "login")
    private String login;

    @JsonProperty(value = "password")
    private String password;
}