package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.entity.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final User user;

    public UserController(final User user) {
        this.user = user;
    }
    @GetMapping("/user")
    public String user() {
        return user.getMiddleName() + " " + user.getFirstName() + " " + user.getLastName()
                + user.getBirthDate().toString();
    }

}
