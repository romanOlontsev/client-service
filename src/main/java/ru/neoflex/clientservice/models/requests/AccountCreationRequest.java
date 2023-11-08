package ru.neoflex.clientservice.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.neoflex.clientservice.utils.groups.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Validated
public class AccountCreationRequest {

    @JsonProperty(value = "bank_id")
    @NotNull(groups = {BankConstraintGroup.class, GosuslugiConstraintGroup.class})
    private Long bankId;

    @JsonProperty(value = "last_name")
    @NotBlank(groups = {BankConstraintGroup.class, GosuslugiConstraintGroup.class})
    private String lastName;

    @JsonProperty(value = "first_name")
    @NotBlank(groups = {MailConstraintGroup.class, BankConstraintGroup.class, GosuslugiConstraintGroup.class})
    private String firstName;

    @JsonProperty(value = "middle_name")
    @NotBlank(groups = {BankConstraintGroup.class, GosuslugiConstraintGroup.class})
    private String middleName;

    @JsonProperty(value = "birth_date")
    @NotNull(groups = {BankConstraintGroup.class, GosuslugiConstraintGroup.class})
    private LocalDate birthDate;

    @JsonProperty(value = "passport")
    @Pattern(regexp = "(^$)|(^\\d{4}\\s\\d{6}$)",
            groups = AllConstraintGroup.class,
            message = "must match the pattern ХХХХ ХХХХХХ")
    @NotBlank(groups = {BankConstraintGroup.class, GosuslugiConstraintGroup.class})
    private String passport;

    @JsonProperty(value = "birthplace")
    @NotBlank(groups = GosuslugiConstraintGroup.class)
    private String birthplace;

    @JsonProperty(value = "phone_number")
    @Pattern(regexp = "(^$)|(^7\\d{10}$)",
            groups = AllConstraintGroup.class,
            message = "must match the pattern 7ХХХХХХХХХХ")
    @NotBlank(groups = {MobileConstraintGroup.class, GosuslugiConstraintGroup.class})
    private String phoneNumber;

    @JsonProperty(value = "email")
    @NotBlank(groups = MailConstraintGroup.class)
    private String email;

    @JsonProperty(value = "registration_address")
    @NotBlank(groups = GosuslugiConstraintGroup.class)
    private String registrationAddress;

    @JsonProperty(value = "residential_address")
    private String residentialAddress;
}
