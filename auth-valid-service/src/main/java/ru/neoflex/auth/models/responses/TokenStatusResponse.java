package ru.neoflex.auth.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.neoflex.auth.models.responses.utils.TokenStatus;

@Getter
@ToString
@EqualsAndHashCode
public class TokenStatusResponse {

    @JsonProperty(value = "token_status")
    private String tokenStatus;

    public TokenStatusResponse(TokenStatus status) {
        this.tokenStatus = status.name();
    }
}
