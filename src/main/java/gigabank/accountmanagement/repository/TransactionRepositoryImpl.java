package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Transaction create(Transaction transaction, BankAccount bankAccount) {
        jdbcTemplate.update("INSERT INTO transaction (value, type, category, createDate, bankAccount_id)" +
                        " VALUES (?, ?, ?, ?, ?)", transaction.getValue(), transaction.getType(),
                transaction.getCategory(), transaction.getCreatedDate(), bankAccount.getId());
        return transaction;
    }

    @Override
    public Transaction findById(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM transaction WHERE id=?",
                new BeanPropertyRowMapper<>(Transaction.class), id);
        //TODO: null result processing
    }

    @Override
    public Transaction update(String id, Transaction transaction) {
        jdbcTemplate.update("UPDATE transaction " +
                "SET value=?, type=?, category=?, createDate=?, bankAccount_id=? WHERE id=?", transaction.getValue(),
                transaction.getType(), transaction.getCategory(), transaction.getCreatedDate(),
                transaction.getBankAccount(), id);
        return transaction;
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("DELETE FROM transaction WHERE id=?", id);
    }

    @Override
    public List<Transaction> findAll() {
        return jdbcTemplate.query("SELECT * FROM transaction", new BeanPropertyRowMapper<>(Transaction.class));
    }
}
