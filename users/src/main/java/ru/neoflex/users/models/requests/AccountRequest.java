package ru.neoflex.users.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Validated
public class AccountRequest {

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
    @Pattern(regexp = "(^$)|(^\\d{4}\\s\\d{6}$)",
            message = "must match the pattern ХХХХ ХХХХХХ")
    private String passport;

    @JsonProperty(value = "birthplace")
    private String birthplace;

    @JsonProperty(value = "phone_number")
    @Pattern(regexp = "(^$)|(^7\\d{10}$)",
            message = "must match the pattern 7ХХХХХХХХХХ")
    private String phoneNumber;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "registration_address")
    private String registrationAddress;

    @JsonProperty(value = "residential_address")
    private String residentialAddress;
}
