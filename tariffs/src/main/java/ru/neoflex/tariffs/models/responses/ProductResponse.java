package ru.neoflex.tariffs.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ProductResponse {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "start_date")
    private LocalDateTime startDate;

    @JsonProperty(value = "end_date")
    private LocalDateTime endDate;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "tariff")
    private TariffResponse tariff;

    @JsonProperty(value = "author")
    private UUID author;

    @JsonProperty(value = "product_version")
    private Long productVersion;
}
