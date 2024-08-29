package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.UserDTO;
import gigabank.accountmanagement.entity.User;
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
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable String id) {
        return userService.findById(id);
    }

    @PostMapping()
    public void createUser(@RequestBody UserDTO userDTO) {
        userService.create(userDTO);
    }

    @PostMapping("/{id}")
    public void updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        userService.update(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.delete(id);
    }

}
