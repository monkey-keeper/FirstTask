package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.User;

import java.math.BigInteger;
import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User create(User user);
    User update(User user);
    void delete(BigInteger id);
    User findById(BigInteger id);
}
