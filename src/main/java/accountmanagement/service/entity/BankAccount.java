package accountmanagement.service.entity;

import java.math.BigDecimal;

/**
 * Информация о банковском счете пользователя
 */
public class BankAccount {
    private String id;
    private BigDecimal balance;
    private User owner;
}
