package ru.neoflex.users.mappers;

import org.mapstruct.Mapper;
import ru.neoflex.users.models.entities.HeaderValidation;
import ru.neoflex.users.models.responses.HeaderValidationResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeaderValidationMapper {

    HeaderValidationResponse headerValidationResponseFromHeaderValidation(HeaderValidation headerValidation);

    List<HeaderValidationResponse> headerValidationResponseListFromHeaderValidationList(List<HeaderValidation> headerValidation);

}
