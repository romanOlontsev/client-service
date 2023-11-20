package ru.neoflex.tariffs.models.responses;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TariffMessage {
    private UUID tariffId;
    private Long tariffVersion;
}
