package ru.neoflex.users.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HeaderValidationRequest {

    @JsonProperty(value = "header")
    @NotBlank
    private String headerName;

    @JsonProperty(value = "required_fields")
    private List<String> requiredFields;

}
