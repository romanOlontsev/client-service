package ru.neoflex.gateway.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.neoflex.gateway.validation.ProductTypeConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ProductRequest {

    @JsonProperty(value = "name")
    @NotBlank
    private String name;

    @JsonProperty(value = "type")
    @ProductTypeConstraint
    private String type;

    @JsonProperty(value = "start_date")
    private LocalDateTime startDate;

    @JsonProperty(value = "end_date")
    private LocalDateTime endDate;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "tariff")
    @NotNull
    private UUID tariff;

    @JsonProperty(value = "author")
    private UUID author;

}
