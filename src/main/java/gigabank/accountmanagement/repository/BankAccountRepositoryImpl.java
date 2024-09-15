package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.mapper.BankAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BankAccount create(BankAccount bankAccount) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO bankAccount (balance) VALUES (?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, bankAccount.getBalance());
            return ps;
        }, keyHolder);
        bankAccount.setId(keyHolder.getKey().toString());
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
    public BankAccount update(BankAccount bankAccount) {
        jdbcTemplate.update("UPDATE bankAccount SET balance=? WHERE id=?", bankAccount.getBalance(), bankAccount.getId());
        return bankAccount;
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("DELETE FROM bankAccount WHERE id=?", id);
    }


}
