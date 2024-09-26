package gigabank.accountmanagement.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Информация о совершенной банковской транзакции
 */
@Data
@NoArgsConstructor
public class Transaction {
    private String id; //TODO: to Long
    private BigDecimal value;
    private TransactionType type;
    private String category;
    private BankAccount bankAccount;
    private LocalDateTime createdDate;

    public Transaction(String id, BigDecimal value, TransactionType type, String category,  LocalDateTime createdDate) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.category = category;
        this.createdDate = createdDate;
    }
}
