package ru.neoflex.clientservice.mappers;

import org.mapstruct.Mapper;
import ru.neoflex.clientservice.models.entities.HeaderValidation;
import ru.neoflex.clientservice.models.responses.HeaderValidationResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeaderValidationMapper {

    HeaderValidationResponse headerValidationResponseFromHeaderValidation(HeaderValidation headerValidation);

    List<HeaderValidationResponse> headerValidationResponseListFromHeaderValidationList(List<HeaderValidation> headerValidation);

}
