package gigabank.accountmanagement.service;

import gigabank.accountmanagement.dao.UserDAO;
import gigabank.accountmanagement.dto.UserDTO;
import gigabank.accountmanagement.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDAO userDAO;

    public List<UserDTO> findAll() {
        return userDAO.findAll().stream()
                .map(UserMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findById(String id) {
        return UserMapper.convertToDTO(userDAO.findById(id));
    }

    public void create(UserDTO userDTO) {
        userDAO.save(UserMapper.convertToEntity(userDTO));
    }

    public void update(String id, UserDTO userDTO) {
        userDAO.save(UserMapper.convertToEntity(userDTO));
    }

    public void delete(String id) {
        userDAO.delete(id);
    }

}
