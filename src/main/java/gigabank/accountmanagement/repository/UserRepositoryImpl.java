package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        jdbcTemplate.update("INSERT INTO userAccount (firstName, middelName, lastname) VALUES (?,?,?)",
                user.getFirstName(), user.getMiddleName(), user.getLastName());
        return user;
    }

    @Override
    public User update(String id, User user) {
        jdbcTemplate.update("UPDATE userAccount SET firstName=?, middelName=?, lastName=? WHERE id=?",
                user.getFirstName(), user.getMiddleName(), user.getLastName(), id);
        return user;
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update("DELETE FROM userAccount WHERE id=?", user.getId());
    }

    @Override
    public User findById(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM userAccount WHERE id=?",
                new BeanPropertyRowMapper<>(User.class), id);
        //TODO: null result processing
    }
}
