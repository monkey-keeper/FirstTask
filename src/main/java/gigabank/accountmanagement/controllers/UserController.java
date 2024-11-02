package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.UserDTO;
import gigabank.accountmanagement.mapper.UserMapper;
import gigabank.accountmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping()
    public List<UserDTO> getAllUsers() {
        return userService.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return UserMapper.toDTO(userService.findById(id));
    }

    @PostMapping()
    public UserDTO createUser(@RequestBody @Valid UserDTO userDTO) {
        kafkaTemplate.send("tpd.topic-name", userDTO);
        return UserMapper.toDTO(userService.create(UserMapper.fromDTO(userDTO)));
    }

    @PostMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
        return UserMapper.toDTO(userService.update(id, UserMapper.fromDTO(userDTO)));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}
