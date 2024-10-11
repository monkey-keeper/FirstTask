package gigabank.accountmanagement.integration.service;

import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    // String userId;
    @Test
    public void createUserTest() { //TODO: нужно связать методы для тестирования, чтобы не засорять БД, только как?
        User user = new User(1L, "first", "middle", "last", LocalDate.now(),
                new ArrayList<>(), "1234567890");
        User newUser = userService.create(user);
        // userId = newUser.getId();
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getMiddleName(), newUser.getMiddleName());
        assertEquals(user.getLastName(), newUser.getLastName());
        assertEquals(user.getBirthDate(), newUser.getBirthDate());
    }

    @Test
    public void findUserTest() {
        List<User> users = userService.findAll();
        assertFalse(users.isEmpty());
    }

//    @Test
//    public void findUserByUsernameTest() {
//        assertEquals(userService.findById("2").getFirstName(), "first");
//        assertEquals(userService.findById("2").getMiddleName(), "middle");
//        assertEquals(userService.findById("2").getLastName(), "last");
//    }

//    @Test
//    public void updateUserTest() {
//        User user = new User("1", "NoFirst", "NoMiddle", "NoLast", LocalDate.now(), new ArrayList<>());
//        User updateUser = userService.update("1", user);
//        assertEquals(user.getFirstName(), updateUser.getFirstName());
//        assertEquals(user.getMiddleName(), updateUser.getMiddleName());
//        assertEquals(user.getLastName(), updateUser.getLastName());
//        assertEquals(user.getBirthDate(), updateUser.getBirthDate());
//    }

//    @Test
//    public void deleteUserTest() { // Работает, но нужно каджый раз менять id
//        userService.delete("1");
//        assertTrue(userService.findById("1") == null);
//    }

}
