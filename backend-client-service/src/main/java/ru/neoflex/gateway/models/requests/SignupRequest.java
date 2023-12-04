package ru.neoflex.gateway.models.requests;

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