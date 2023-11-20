package ru.neoflex.products.models.requests;

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
