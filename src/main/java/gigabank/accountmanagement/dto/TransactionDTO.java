package gigabank.accountmanagement.dto;

import gigabank.accountmanagement.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String transactionId;
    private BigDecimal value;
    private TransactionType type;
    private String category;
    private BankAccountDTO bankAccount;
}
