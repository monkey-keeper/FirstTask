package gigabank.accountmanagement.entity;

import jakarta.persistence.*;
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
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_value")
    private BigDecimal value;
    @Column(name = "transaction_type")
    private TransactionType type;
    private String category;
    @ManyToOne
    private BankAccount bankAccount;
    private LocalDateTime createdDate;

}
