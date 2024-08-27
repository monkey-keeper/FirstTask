package gigabank.accountmanagement.dao;

import gigabank.accountmanagement.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {

    public List<User> findAll() {
        // тут должна быть реализация SQL
        return null;
    }

    public User findById(String id) {
        // тут должна быть реализация SQL
        return null;
    }

    public void save(User user) {
        // тут должна быть реализация SQL
    }

    public void update(String id, User user) {
        // тут должна быть реализация SQL

    }

    public void delete(String id) {
        // тут должна быть реализация SQL

    }



}
