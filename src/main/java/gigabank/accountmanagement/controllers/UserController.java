package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.UserDTO;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.mapper.UserMapper;
import gigabank.accountmanagement.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<UserDTO> getAllUsers() {
        return userService.findAll().stream()
                .map(UserMapper::convertToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable String id) {
        return UserMapper.convertToDTO(userService.findById(id));
    }

    @PostMapping()
    public void createUser(@RequestBody UserDTO userDTO) {
        userService.create(UserMapper.convertToEntity(userDTO));
    }

    @PostMapping("/{id}")
    public void updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        userService.update(id, UserMapper.convertToEntity(userDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.delete(id);
    }

}
