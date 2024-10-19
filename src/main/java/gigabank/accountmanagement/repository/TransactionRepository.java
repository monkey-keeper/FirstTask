package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t " +
            "WHERE (:category IS NULL OR t.category = :category) " +
            "AND (:transactionType IS NULL OR t.type = :transactionType)")
    List<Transaction> findByCategoryAndType(@Param("category") String category,
                                            @Param("transactionType") TransactionType transactionType);

    @Query("SELECT t FROM Transaction t WHERE t.value = " +
            "(SELECT MAX(t2.value) FROM Transaction t2 WHERE t2.bankAccount.id = :bankAccountId)")
    Transaction findLargestTransactionByBankAccount(@Param("bankAccountId") Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.value = " +
            "(SELECT MIN(t2.value) FROM Transaction t2 WHERE t2.bankAccount.id = :bankAccountId)")
    Transaction findSmallestTransactionByBankAccount(@Param("bankAccountId") Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.value = " +
            "(SELECT AVG(t2.value) FROM Transaction t2 WHERE t2.bankAccount.id = :bankAccountId)")
    Transaction findAverageTransactionByBankAccount(@Param("bankAccountId") Long accountId);

    @Query("SELECT SUM(t.value) FROM Transaction t WHERE t.category = :category AND t.bankAccount.id = :bankAccountId")
    BigDecimal findSumTransactionByCategory(@Param("category") String category,
                                            @Param("bankAccountId") Long accountId);

    @Query("SELECT SUM(t.value) FROM Transaction t WHERE "
            + "( t.createdDate >= :startDate) "
            + "AND ( t.createdDate <= :endDate) "
            + "AND t.bankAccount.id = :accountId")
    BigDecimal findSumByDateRange(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate,
                                  @Param("accountId") Long accountId);


}
