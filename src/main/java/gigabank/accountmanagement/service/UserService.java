package gigabank.accountmanagement.service;

import gigabank.accountmanagement.dao.UserDAO;
import gigabank.accountmanagement.dto.UserDTO;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDAO userDAO;

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User findById(String id) {
        return userDAO.findById(id);
    }

    public void create(User user) {
        userDAO.save(user);
    }

    public void update(String id, User user) {
        userDAO.save(user);
    }

    public void delete(String id) {
        userDAO.delete(id);
    }

}
