package accountmanagement.service.entity;

import java.math.BigDecimal;

/**
 * Информация о совершенной банковской транзакции
 */
public class Transaction {
    private String id;
    private BigDecimal value;
    private TransactionType type;
    private String category;
}
