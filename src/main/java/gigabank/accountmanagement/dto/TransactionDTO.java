package gigabank.accountmanagement.dto;

import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.validation.TypeContain;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long transactionId;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than zero")
    private BigDecimal value;

    @NotNull(message = "Type cannot be null")
    @TypeContain
    private TransactionType type;

    @NotNull(message = "Category cannot be null")
    private String category;

    @NotNull(message = "BankAccount cannot be null")
    @Valid
    private BankAccountDTO bankAccount;
}
