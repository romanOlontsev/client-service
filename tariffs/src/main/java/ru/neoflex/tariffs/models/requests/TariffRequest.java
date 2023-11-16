package ru.neoflex.tariffs.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class TariffRequest {

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

}
