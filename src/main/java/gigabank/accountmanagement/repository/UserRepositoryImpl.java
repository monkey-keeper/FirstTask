package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM userAccount", new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO userAccount (firstName, middelName, lastname) VALUES (?,?,?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getMiddleName());
            ps.setString(3, user.getLastName());
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().toString());
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update("UPDATE userAccount SET firstName=?, middelName=?, lastName=? WHERE id=?",
                user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getId());
        return user;
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("DELETE FROM userAccount WHERE id=?", id);
    }

    @Override
    public User findById(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM userAccount WHERE id=?",
                new BeanPropertyRowMapper<>(User.class), id);
        //TODO: null result processing
    }
}
