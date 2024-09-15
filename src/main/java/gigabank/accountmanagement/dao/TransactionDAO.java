package gigabank.accountmanagement.dao;

import gigabank.accountmanagement.entity.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
public class TransactionDAO {
    public List<Transaction> getTransactions() {
        // тут должна быть реализация SQL
        return null;
    }

    public Transaction getTransactionById(String id) {
        // тут должна быть реализация SQL
        return null;
    }

    public void createTransaction(Transaction transaction) {
        // тут должна быть реализация SQL
    }

    public void updateTransaction(String id, Transaction transaction) {
        // тут должна быть реализация SQL
    }

    public void deleteTransaction(String id) {
        // тут должна быть реализация SQL
    }

}
