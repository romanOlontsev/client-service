package ru.neoflex.clientservice.models.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HeaderValidationRequest {

    private String headerName;

    private List<String> requiredFields;

}
