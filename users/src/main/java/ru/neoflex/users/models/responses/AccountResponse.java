package ru.neoflex.users.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AccountResponse {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "bank_id")
    private Long bankId;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "middle_name")
    private String middleName;

    @JsonProperty(value = "birth_date")
    private LocalDate birthDate;

    @JsonProperty(value = "passport")
    private String passport;

    @JsonProperty(value = "birthplace")
    private String birthplace;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "registration_address")
    private String registrationAddress;

    @JsonProperty(value = "residential_address")
    private String residentialAddress;
}
