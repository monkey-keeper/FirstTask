package gigabank.accountmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация о банковском счете пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    private String id;
    private BigDecimal balance;
    private User owner;
    private List<Transaction> transactions;

}
