package ru.neoflex.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SignupRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User userFromSignupRequest(SignupRequest request);
}
