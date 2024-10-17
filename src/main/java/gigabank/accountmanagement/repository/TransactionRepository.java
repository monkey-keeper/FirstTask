package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE (:category IS NULL OR t.category = :category) AND (:transactionType IS NULL OR t.type = :transactionType)")
    List<Transaction> findByCategoryAndType(@Param("category") String category,
                                            @Param("transactionType") TransactionType transactionType);
    List<Transaction> findByCategory(String category);

}
