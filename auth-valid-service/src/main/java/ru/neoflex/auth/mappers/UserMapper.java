package ru.neoflex.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.userdetails.UserDetails;
import ru.neoflex.auth.models.entity.User;
import ru.neoflex.auth.models.requests.SignupRequest;
import ru.neoflex.auth.models.responses.UserDetailsResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User userFromSignupRequest(SignupRequest request);

    UserDetailsResponse userDetailsResponseFromUserDetails(UserDetails userDetails);
}
