package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User create(User user);
    User update(String id, User user);
    void delete(User user);
    User findById(String id);
}
