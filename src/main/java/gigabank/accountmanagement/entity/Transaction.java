package gigabank.accountmanagement.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Информация о совершенной банковской транзакции
 */
@Data
public class Transaction{
    @NonNull
    private String id;
    @NonNull
    private BigDecimal value;
    @NonNull
    private TransactionType type;
    @NonNull
    private String category;
    @NonNull
    private BankAccount bankAccount;
    @NonNull
    private LocalDateTime createdDate;
}
