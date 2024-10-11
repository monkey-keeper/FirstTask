package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.UserDTO;
import gigabank.accountmanagement.mapper.UserMapper;
import gigabank.accountmanagement.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

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
    public UserDTO createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorMessage.toString());
        }
        return UserMapper.toDTO(userService.create(UserMapper.fromDTO(userDTO)));
    }

    @PostMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorMessage.toString());
        }
        return UserMapper.toDTO(userService.update(id, UserMapper.fromDTO(userDTO)));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}
