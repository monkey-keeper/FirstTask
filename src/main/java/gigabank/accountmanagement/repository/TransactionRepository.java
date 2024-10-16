package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.Transaction;

import java.math.BigInteger;
import java.util.List;

public interface TransactionRepository {
    Transaction create(Transaction transaction);
    Transaction findById(BigInteger id);
    Transaction update(Transaction transaction);
    void delete(BigInteger id);
    List<Transaction> findAll();
    List<Transaction> findByCategoryAndType(String category, String type);
}
