package ru.neoflex.auth.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtAuthenticationResponse {

    @JsonProperty(value = "token")
    private String token;

    @JsonProperty(value = "id")
    private Long id;
}