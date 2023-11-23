package ru.neoflex.auth.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class TokenValidityRequest {

    @JsonProperty(value = "token")
    private String token;
}
