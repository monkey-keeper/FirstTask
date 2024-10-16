package gigabank.accountmanagement.service;

import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(BigInteger.valueOf(id));
    }

    public User create(User user) {
        return userRepository.create(user);
    }

    public User update(Long id, User user) {
        user.setId(id);
        return userRepository.update(user);
    }

    public void delete(Long id) {
        userRepository.delete(BigInteger.valueOf(id));
    }

}
