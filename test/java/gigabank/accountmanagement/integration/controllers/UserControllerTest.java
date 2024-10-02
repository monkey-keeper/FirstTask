package gigabank.accountmanagement.integration.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("1", "first_1", "middle_2", "last_3", LocalDate.now(),
                new ArrayList<>());
        String serializedUser = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedUser))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        User newUser = objectMapper.readValue(json, User.class);
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getMiddleName(), newUser.getMiddleName());
        assertEquals(user.getLastName(), newUser.getLastName());
        assertEquals(user.getBirthDate(), newUser.getBirthDate());

    }

    @Test
    public void testGetAllUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/user"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<User> users = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<User>>() {});

        assertFalse(users.isEmpty());

        User firstUser = users.get(0);

        assertNotNull(firstUser.getId());

        User firstUserByRepository = userRepository.findAll().get(0);

        assertEquals(firstUser.getId(), firstUserByRepository.getId());
        assertEquals(firstUser.getFirstName(), firstUserByRepository.getFirstName());
        assertEquals(firstUser.getMiddleName(), firstUserByRepository.getMiddleName());
        assertEquals(firstUser.getLastName(), firstUserByRepository.getLastName());
        assertEquals(firstUser.getBirthDate(), firstUserByRepository.getBirthDate());
    }

    @Test
    public void testGetUserById() throws Exception {
        String id = "4";
        MvcResult result = mockMvc.perform(get(String.format("/user/%s", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        User user = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertNotNull(user);
        assertEquals(id, user.getId());

        User userByRepository = userRepository.findById(BigInteger.valueOf(Long.parseLong(id)));
        assertNotNull(userByRepository);
        assertEquals(user.getFirstName(), userByRepository.getFirstName());
        assertEquals(user.getMiddleName(), userByRepository.getMiddleName());
        assertEquals(user.getLastName(), userByRepository.getLastName());
        assertEquals(user.getBirthDate(), userByRepository.getBirthDate());

    }

    @Test
    public void testUpdateUser() throws Exception {
        String id = "5";
        User user = new User("1", "tsrif", "elddim", "tsal", LocalDate.now(),
                new ArrayList<>());
        String serializedUser = objectMapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post(String.format("/user/%s", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedUser))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        User updateUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertEquals(id, updateUser.getId());
        assertEquals(user.getFirstName(), updateUser.getFirstName());
        assertEquals(user.getMiddleName(), updateUser.getMiddleName());
        assertEquals(user.getLastName(), updateUser.getLastName());
        assertEquals(user.getBirthDate(), updateUser.getBirthDate());

        User updatedUserByRepository = userRepository.findById(BigInteger.valueOf(Long.parseLong(id)));

        assertNotNull(updatedUserByRepository);
        assertEquals(updateUser.getFirstName(), updatedUserByRepository.getFirstName());
        assertEquals(updateUser.getMiddleName(), updatedUserByRepository.getMiddleName());
        assertEquals(updateUser.getLastName(), updatedUserByRepository.getLastName());
        assertEquals(updateUser.getBirthDate(), updatedUserByRepository.getBirthDate());

    }

    @Test
    public void testDeleteUser() throws Exception {
        String id = "5";
        MvcResult result = mockMvc.perform(delete(String.format("/user/%s", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertNull(objectMapper.readValue(result.getResponse().getContentAsString(), User.class));
    }



}
