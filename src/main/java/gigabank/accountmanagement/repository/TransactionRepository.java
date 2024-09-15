package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;

import java.util.List;

public interface TransactionRepository {
    Transaction create(Transaction transaction, BankAccount account);
    Transaction findById(String id);
    Transaction update(Transaction transaction);
    void delete(String id);
    List<Transaction> findAll();
    List<Transaction> findByCategoryAndType(String category, String type);
}
