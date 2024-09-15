package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Transaction create(Transaction transaction, BankAccount bankAccount) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO transaction (value, type, category, createDate, bankAccount_id)" +
                " VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setObject(1, transaction.getValue());
            ps.setString(2, String.valueOf(transaction.getType()));
            ps.setString(3, transaction.getCategory());
            ps.setObject(4, transaction.getCreatedDate());
            ps.setObject(5, bankAccount.getId());
            return ps;
        }, keyHolder);
        transaction.setId(keyHolder.getKey().toString());
        return transaction;
    }

    @Override
    public Transaction findById(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM transaction WHERE id=?",
                new BeanPropertyRowMapper<>(Transaction.class), id);
        //TODO: null result processing
    }

    @Override
    public List<Transaction> findByCategoryAndType(String category, String type) {
        StringBuilder sql = new StringBuilder("SELECT * FROM transaction");
        if (category == null && type == null) {
            return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Transaction.class));
        }
        if (type != null) {
            sql.append(" WHERE type=?");
            if (category == null) {
                return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Transaction.class), type);
            }
            sql.append(" AND category=?");
            return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Transaction.class), type,
                    category);
        }
        sql.append(" WHERE category=?");
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Transaction.class), category);
    }

    @Override
    public Transaction update(Transaction transaction) {
        jdbcTemplate.update("UPDATE transaction " +
                "SET value=?, type=?, category=?, createDate=?, bankAccount_id=? WHERE id=?", transaction.getValue(),
                transaction.getType(), transaction.getCategory(), transaction.getCreatedDate(),
                transaction.getBankAccount(), transaction.getId());
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
