package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.mapper.BankAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BankAccount save(BankAccount bankAccount) {
        jdbcTemplate.update("INSERT INTO bankAccount (balance) VALUES (?)", bankAccount.getBalance());
        return bankAccount;
    }

    @Override
    public BankAccount findById(String id) {
        return (BankAccount) jdbcTemplate.query("SELECT * FROM bankAccount WHERE id=?",
                new BeanPropertyRowMapper<>(BankAccount.class), id);
        //TODO: null result processing
    }

    @Override
    public List<BankAccount> findAll() {
        return jdbcTemplate.query("SELECT * FROM bankAccount",
                new BeanPropertyRowMapper<>(BankAccount.class));
    }

    @Override
    public BankAccount update(String id, BankAccount bankAccount) {
        jdbcTemplate.update("UPDATE bankAccount SET balance=? WHERE id=?", bankAccount.getBalance(), id);
        return bankAccount;
    }

    @Override
    public void delete(BankAccount bankAccount) {
        jdbcTemplate.update("DELETE FROM bankAccount WHERE id=?", bankAccount.getId());
    }


}
