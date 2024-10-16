package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.mapper.BankAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
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
        bankAccount.setId((Long) keyHolder.getKeys().get("id"));

        String sqlUserBankAccount = "INSERT INTO user_bankAccount (user_Id, bankAccount_Id) VALUES (?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlUserBankAccount);
            ps.setLong(1, bankAccount.getOwner().getId());
            ps.setLong(2, bankAccount.getId());
            return ps;
        });

        return bankAccount;
    }

    @Override
    public BankAccount findById(BigInteger id) {
        String sql = "SELECT ba.id AS bank_account_id, ba.balance, ua.id AS user_id, ua.firstName, " +
                "ua.middleName, ua.lastName, ua.birthdate, ua.phonenumber FROM bankaccount ba JOIN user_bankaccount uba ON ba.id = uba.bankaccount_id " +
                "JOIN useraccount ua ON ua.id = uba.user_id WHERE ba.id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, BankAccountRepositoryImpl::mapRow);
    }

    @Override
    public List<BankAccount> findAll() {
        String sql = "SELECT ba.id AS bank_account_id, ba.balance, ua.id AS user_id, ua.firstName, " +
                "ua.middleName, ua.lastName, ua.birthdate, ua.phonenumber FROM bankaccount ba JOIN user_bankaccount uba ON ba.id = uba.bankaccount_id " +
                "JOIN useraccount ua ON ua.id = uba.user_id";
        return jdbcTemplate.query(sql, BankAccountRepositoryImpl::mapRow);
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        String sql = "UPDATE bankAccount SET balance=? WHERE id=?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBigDecimal(1, bankAccount.getBalance());
            ps.setLong(2, bankAccount.getId());
            return ps;
        });

        User owner = bankAccount.getOwner();
        if (owner != null) {
            String updateOwnerSQL = "UPDATE user_bankaccount SET user_id = ? WHERE bankaccount_id = ?";
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(updateOwnerSQL);
                ps.setLong(1, owner.getId());
                ps.setLong(2, bankAccount.getId());
                return ps;
            });
        }

        return bankAccount;
    }

    @Override
    public void delete(BigInteger id) {
        jdbcTemplate.update("DELETE FROM user_bankaccount WHERE bankaccount_id=?", id);
        jdbcTemplate.update("DELETE FROM bankAccount WHERE id=?", id);
    }

    private static BankAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Извлекаем дату рождения как LocalDate
        LocalDate birthDate = rs.getTimestamp("birthdate").toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Создаём пользователя с датой рождения
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setFirstName(rs.getString("firstName"));
        user.setMiddleName(rs.getString("middleName"));
        user.setLastName(rs.getString("lastName"));
        user.setBirthDate(birthDate);  // Передаём LocalDate
        user.setPhoneNumber(rs.getString("phoneNumber"));

        // Создаём банковский счёт
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(rs.getLong("bank_account_id"));
        bankAccount.setBalance(rs.getBigDecimal("balance"));
        bankAccount.setOwner(user);  // Связываем пользователя с банковским счётом

        return bankAccount;
    }

}
