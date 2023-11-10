package ru.neoflex.clientservice.models.responses;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class HeaderValidationResponse {

    private Long id;

    private String headerName;

    private List<String> requiredFields;
}
