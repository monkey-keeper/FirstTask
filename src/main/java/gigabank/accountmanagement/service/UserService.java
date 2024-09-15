package gigabank.accountmanagement.service;

import gigabank.accountmanagement.dao.UserDAO;
import gigabank.accountmanagement.dto.UserDTO;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.mapper.UserMapper;
import gigabank.accountmanagement.repository.UserRepository;
import gigabank.accountmanagement.repository.UserRepositoryImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id);
    }

    public User create(User user) {
        return userRepository.create(user);
    }

    public User update(String id, User user) {
        user.setId(id);
        return userRepository.update(user);
    }

    public void delete(String id) {
        userRepository.delete(id);
    }

}
