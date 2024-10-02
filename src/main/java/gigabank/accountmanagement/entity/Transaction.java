package gigabank.accountmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Информация о совершенной банковской транзакции
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String id; //TODO: to Long
    private BigDecimal value;
    private TransactionType type;
    private String category;
    private BankAccount bankAccount;
    private LocalDateTime createdDate;

}
