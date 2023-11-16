package ru.neoflex.tariffs.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.neoflex.tariffs.models.entities.Tariff;
import ru.neoflex.tariffs.models.requests.TariffRequest;
import ru.neoflex.tariffs.models.responses.TariffResponse;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TariffMapper {

    TariffResponse tariffResponseFromTariff(Tariff tariff);

    Tariff tariffFromTariffRequest(TariffRequest request);

    List<TariffResponse> tariffResponseListFromTariffList(List<Tariff> tariffList);

    void updateTariffFromTariffRequest(@MappingTarget Tariff product, TariffRequest request);

    void updateTariff(@MappingTarget Tariff target, Tariff source);
}
