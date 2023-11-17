package ru.neoflex.gateway.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class TariffResponse {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "start_date")
    private LocalDateTime startDate;

    @JsonProperty(value = "end_date")
    private LocalDateTime endDate;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "rate")
    private Double rate;

    @JsonProperty(value = "version")
    private Long version;
}
