package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User create(User user);
    User update(User user);
    void delete(String id);
    User findById(String id);
}
