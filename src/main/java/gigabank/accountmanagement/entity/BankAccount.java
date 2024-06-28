package gigabank.accountmanagement.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * Информация о банковском счете пользователя
 */
@Data
@ToString(of = {"id", "balance"})
public class BankAccount {
    @NonNull
    private String id;
    @NonNull
    private BigDecimal balance;
    @NonNull
    private User owner;
    @NonNull
    private List<Transaction> transactions;
}
