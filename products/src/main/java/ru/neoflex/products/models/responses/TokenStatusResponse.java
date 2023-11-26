package ru.neoflex.products.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.neoflex.products.models.responses.utils.TokenStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenStatusResponse {

    @JsonProperty(value = "token_status")
    private TokenStatus tokenStatus;
}
