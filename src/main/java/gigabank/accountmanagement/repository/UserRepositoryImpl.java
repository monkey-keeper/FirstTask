package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM userAccount", new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO userAccount (firstName, middleName, lastname, birthdate, phoneNumber) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getMiddleName());
            ps.setString(3, user.getLastName());
            ZonedDateTime zonedDateTime = user.getBirthDate().atStartOfDay(ZoneId.systemDefault());
            ps.setTimestamp(4, Timestamp.from(zonedDateTime.toInstant()));
            ps.setString(5, user.getPhoneNumber());
            return ps;
        }, keyHolder);
        user.setId((Long) keyHolder.getKeys().get("id"));
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE userAccount SET firstName=?, middleName=?, lastName=?, birthdate=?, phoneNumber=? WHERE id=?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getMiddleName());
            ps.setString(3, user.getLastName());
            ZonedDateTime zonedDateTime = user.getBirthDate().atStartOfDay(ZoneId.systemDefault());
            ps.setTimestamp(4, Timestamp.from(zonedDateTime.toInstant()));
            ps.setString(5, user.getPhoneNumber());
            ps.setLong (6, user.getId());
            return ps;
        });
        return user;
    }

    @Override
    public void delete(BigInteger id) {
        jdbcTemplate.update("DELETE FROM userAccount WHERE id=?", id);
    }

    @Override
    public User findById(BigInteger id) {
        return jdbcTemplate.queryForObject("SELECT * FROM userAccount WHERE id=?",
                new BeanPropertyRowMapper<>(User.class), id);
        //TODO: null result processing
    }
}
