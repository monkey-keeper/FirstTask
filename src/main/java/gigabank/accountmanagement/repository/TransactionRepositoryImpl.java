package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.entity.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Transaction create(Transaction transaction) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO transaction (value, type, category, createDate, bankAccount_id)" +
                " VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, transaction.getValue());
            ps.setString(2, String.valueOf(transaction.getType()));
            ps.setString(3, transaction.getCategory());
            ps.setTimestamp(4, Timestamp.valueOf(transaction.getCreatedDate()));
            ps.setLong(5, Long.parseLong(transaction.getBankAccount().getId()));
            return ps;
        }, keyHolder);
        transaction.setId(keyHolder.getKeys().get("id").toString());
        return transaction;
    }

    @Override
    public Transaction findById(BigInteger id) {
        String sql = "SELECT t.id AS transaction_id, t.value, t.type, t.category, t.createDate, " +
                "b.id AS bankaccount_id, b.balance, u.id AS user_id, u.firstName, u.middleName, u.lastName, u.birthdate " +
                "FROM transaction t " +
                "JOIN bankAccount b ON t.bankAccount_id = b.id " +
                "JOIN user_bankaccount ub ON b.id = ub.bankaccount_id " +
                "JOIN useraccount u ON u.id = ub.user_id WHERE t.id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                    LocalDate birthDate = rs.getTimestamp("birthdate").toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    // Создаём объект UserAccount
                    User user = new User();
                    user.setId(rs.getString("user_id"));
                    user.setFirstName(rs.getString("firstname"));
                    user.setMiddleName(rs.getString("middlename"));
                    user.setLastName(rs.getString("lastname"));
                    user.setBirthDate(birthDate);

                    // Создаём объект BankAccount
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setId(rs.getString("bankaccount_id"));
                    bankAccount.setBalance(rs.getBigDecimal("balance"));
                    bankAccount.setOwner(user);

                    // Создаём объект Transaction
                    Transaction transaction = new Transaction();
                    transaction.setId(rs.getString("transaction_id"));
                    transaction.setValue(rs.getBigDecimal("value"));
                    transaction.setType(TransactionType.valueOf(rs.getString("type")));
                    transaction.setCategory(rs.getString("category"));
                    transaction.setCreatedDate(rs.getTimestamp("createDate").toLocalDateTime());
                    transaction.setBankAccount(bankAccount);

                    return transaction;
                });
    }

    @Override
    public List<Transaction> findByCategoryAndType(String category, String type) {
        StringBuilder sql = new StringBuilder("SELECT t.id AS transaction_id, t.value, t.type, t.category, t.createDate, " +
                "b.id AS bankaccount_id, b.balance, u.id AS user_id, u.firstName, u.middleName, u.lastName, u.birthdate " +
                "FROM transaction t " +
                "JOIN bankAccount b ON t.bankAccount_id = b.id " +
                "JOIN user_bankaccount ub ON b.id = ub.bankaccount_id " +
                "JOIN useraccount u ON u.id = ub.user_id");
        if (category == null && type == null) {
            return findAll();
        }
        if (type != null) {
            sql.append(" WHERE type=?");
            if (category == null) {
                return jdbcTemplate.query(sql.toString(),  new Object[]{type}, (rs, rowNum) -> {
                    LocalDate birthDate = rs.getTimestamp("birthdate").toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    // Создаём объект UserAccount
                    User user = new User();
                    user.setId(rs.getString("user_id"));
                    user.setFirstName(rs.getString("firstname"));
                    user.setMiddleName(rs.getString("middlename"));
                    user.setLastName(rs.getString("lastname"));
                    user.setBirthDate(birthDate);

                    // Создаём объект BankAccount
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setId(rs.getString("bankaccount_id"));
                    bankAccount.setBalance(rs.getBigDecimal("balance"));
                    bankAccount.setOwner(user);

                    // Создаём объект Transaction
                    Transaction transaction = new Transaction();
                    transaction.setId(rs.getString("transaction_id"));
                    transaction.setValue(rs.getBigDecimal("value"));
                    transaction.setType(TransactionType.valueOf(rs.getString("type")));
                    transaction.setCategory(rs.getString("category"));
                    transaction.setCreatedDate(rs.getTimestamp("createDate").toLocalDateTime());
                    transaction.setBankAccount(bankAccount);

                    return transaction;
                });
            } else {
                sql.append(" AND category=?");
                return jdbcTemplate.query(sql.toString(),  new Object[]{type, category}, (rs, rowNum) -> {
                    LocalDate birthDate = rs.getTimestamp("birthdate").toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    // Создаём объект UserAccount
                    User user = new User();
                    user.setId(rs.getString("user_id"));
                    user.setFirstName(rs.getString("firstname"));
                    user.setMiddleName(rs.getString("middlename"));
                    user.setLastName(rs.getString("lastname"));
                    user.setBirthDate(birthDate);

                    // Создаём объект BankAccount
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setId(rs.getString("bankaccount_id"));
                    bankAccount.setBalance(rs.getBigDecimal("balance"));
                    bankAccount.setOwner(user);

                    // Создаём объект Transaction
                    Transaction transaction = new Transaction();
                    transaction.setId(rs.getString("transaction_id"));
                    transaction.setValue(rs.getBigDecimal("value"));
                    transaction.setType(TransactionType.valueOf(rs.getString("type")));
                    transaction.setCategory(rs.getString("category"));
                    transaction.setCreatedDate(rs.getTimestamp("createDate").toLocalDateTime());
                    transaction.setBankAccount(bankAccount);

                    return transaction;
                });
            }
        } else {
            sql.append(" WHERE category=?");
            return jdbcTemplate.query(sql.toString(), new Object[]{category}, (rs, rowNum) -> {
                LocalDate birthDate = rs.getTimestamp("birthdate").toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                // Создаём объект UserAccount
                User user = new User();
                user.setId(rs.getString("user_id"));
                user.setFirstName(rs.getString("firstname"));
                user.setMiddleName(rs.getString("middlename"));
                user.setLastName(rs.getString("lastname"));
                user.setBirthDate(birthDate);

                // Создаём объект BankAccount
                BankAccount bankAccount = new BankAccount();
                bankAccount.setId(rs.getString("bankaccount_id"));
                bankAccount.setBalance(rs.getBigDecimal("balance"));
                bankAccount.setOwner(user);

                // Создаём объект Transaction
                Transaction transaction = new Transaction();
                transaction.setId(rs.getString("transaction_id"));
                transaction.setValue(rs.getBigDecimal("value"));
                transaction.setType(TransactionType.valueOf(rs.getString("type")));
                transaction.setCategory(rs.getString("category"));
                transaction.setCreatedDate(rs.getTimestamp("createDate").toLocalDateTime());
                transaction.setBankAccount(bankAccount);

                return transaction;
            });
        }
    }

    @Override
    public Transaction update(Transaction transaction) {
        String sql = "UPDATE transaction " +
                "SET value=?, type=?, category=?, bankAccount_id=? WHERE id=?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBigDecimal(1, transaction.getValue());
            ps.setString(2, transaction.getType().name());
            ps.setString(3, transaction.getCategory());
            ps.setLong(4, Long.parseLong(transaction.getBankAccount().getId()));
            ps.setLong(5, Long.parseLong(transaction.getId()));
            return ps;
        } );

        return transaction;
    }

    @Override
    public void delete(BigInteger id) {
        jdbcTemplate.update("DELETE FROM transaction WHERE id=?", id);
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT t.id AS transaction_id, t.value, t.type, t.category, t.createDate, " +
                "b.id AS bankaccount_id, b.balance, u.id AS user_id, u.firstName, u.middleName, u.lastName, u.birthdate " +
                "FROM transaction t " +
                "JOIN bankAccount b ON t.bankAccount_id = b.id " +
                "JOIN user_bankaccount ub ON b.id = ub.bankaccount_id " +
                "JOIN useraccount u ON u.id = ub.user_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LocalDate birthDate = rs.getTimestamp("birthdate").toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // Создаём объект UserAccount
            User user = new User();
            user.setId(rs.getString("user_id"));
            user.setFirstName(rs.getString("firstname"));
            user.setMiddleName(rs.getString("middlename"));
            user.setLastName(rs.getString("lastname"));
            user.setBirthDate(birthDate);


            // Создаём объект BankAccount
            BankAccount bankAccount = new BankAccount();
            bankAccount.setId(rs.getString("bankaccount_id"));
            bankAccount.setBalance(rs.getBigDecimal("balance"));
            bankAccount.setOwner(user);

            // Создаём объект Transaction
            Transaction transaction = new Transaction();
            transaction.setId(rs.getString("transaction_id"));
            transaction.setValue(rs.getBigDecimal("value"));
            transaction.setType(TransactionType.valueOf(rs.getString("type")));
            transaction.setCategory(rs.getString("category"));
            transaction.setCreatedDate(rs.getTimestamp("createDate").toLocalDateTime());
            transaction.setBankAccount(bankAccount);

            return transaction;
        });
    }

}
