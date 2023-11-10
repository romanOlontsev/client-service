package ru.neoflex.clientservice.mappers;

import org.mapstruct.Mapper;
import ru.neoflex.clientservice.models.entities.HeaderValidation;
import ru.neoflex.clientservice.models.requests.HeaderValidationRequest;

@Mapper(componentModel = "spring")
public interface HeaderValidationMapper {

    HeaderValidation headerValidationFromHeaderValidationRequest(HeaderValidationRequest request);

}
