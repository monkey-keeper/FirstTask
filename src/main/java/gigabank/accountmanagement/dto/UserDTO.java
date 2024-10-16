package gigabank.accountmanagement.dto;

import gigabank.accountmanagement.validation.AgeConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name must be less than 50 characters")
    private String middleName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    @NotNull(message = "Birth date cannot be null")
    @AgeConstraint
    private LocalDate birthDate;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Phone number must be a valid format with 7 to 15 digits")
    private String phoneNumber;
}
