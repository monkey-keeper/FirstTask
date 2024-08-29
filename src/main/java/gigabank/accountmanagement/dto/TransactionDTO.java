package gigabank.accountmanagement.dto;

import gigabank.accountmanagement.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String transactionId;
    private BigDecimal value;
    private TransactionType type;
    private String category;
}
