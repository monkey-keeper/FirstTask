package gigabank.accountmanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    private Long id;

    @DecimalMin(value = "0.0", message = "Balance must be greater than zero")
    private BigDecimal balance;

    @NotNull(message = "Owner cannot be null")
    @Valid
    private UserDTO owner;
}
