package gigabank.accountmanagement.integration.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gigabank.accountmanagement.dto.UserDTO;
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
        UserDTO user = new UserDTO(1L, "first_1", "middle_2", "last_3",
                LocalDate.now().minusYears(20), "88005553535");
        String serializedUser = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedUser))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        UserDTO newUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getMiddleName(), newUser.getMiddleName());
        assertEquals(user.getLastName(), newUser.getLastName());
        assertEquals(user.getBirthDate(), newUser.getBirthDate());
        assertEquals(user.getPhoneNumber(), newUser.getPhoneNumber());

        Long id = newUser.getId();

        assertNotNull(id);

        User userByRepository = userRepository.findById(BigInteger.valueOf(id));

        assertNotNull(userByRepository);
        assertEquals(user.getFirstName(), userByRepository.getFirstName());
        assertEquals(user.getMiddleName(), userByRepository.getMiddleName());
        assertEquals(user.getLastName(), userByRepository.getLastName());
        assertEquals(user.getBirthDate(), userByRepository.getBirthDate());
        assertEquals(user.getPhoneNumber(), userByRepository.getPhoneNumber());

        MvcResult result2 = mockMvc.perform(get(String.format("/user/%d", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        UserDTO getUser = objectMapper.readValue(result2.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(getUser);
        assertEquals(id, getUser.getId());
        assertEquals(user.getFirstName(), getUser.getFirstName());
        assertEquals(user.getMiddleName(), getUser.getMiddleName());
        assertEquals(user.getLastName(), getUser.getLastName());
        assertEquals(user.getBirthDate(), getUser.getBirthDate());
        assertEquals(user.getPhoneNumber(), getUser.getPhoneNumber());

        UserDTO user2 = new UserDTO(1L, "tsrif", "elddim", "tsal",
                LocalDate.now().minusYears(30), "1234567890");
        String serializedUser2 = objectMapper.writeValueAsString(user2);
        MvcResult result3 = mockMvc.perform(post(String.format("/user/%d", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedUser2))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        UserDTO updateUser = objectMapper.readValue(result3.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(updateUser);
        assertEquals(id, updateUser.getId());
        assertEquals(user2.getFirstName(), updateUser.getFirstName());
        assertEquals(user2.getMiddleName(), updateUser.getMiddleName());
        assertEquals(user2.getLastName(), updateUser.getLastName());
        assertEquals(user2.getBirthDate(), updateUser.getBirthDate());
        assertEquals(user2.getPhoneNumber(), updateUser.getPhoneNumber());

        User updatedUserByRepository = userRepository.findById(BigInteger.valueOf(id));

        assertNotNull(updatedUserByRepository);
        assertEquals(id, updatedUserByRepository.getId());
        assertEquals(user2.getFirstName(), updatedUserByRepository.getFirstName());
        assertEquals(user2.getMiddleName(), updatedUserByRepository.getMiddleName());
        assertEquals(user2.getLastName(), updatedUserByRepository.getLastName());
        assertEquals(user2.getBirthDate(), updatedUserByRepository.getBirthDate());
        assertEquals(user2.getPhoneNumber(), updatedUserByRepository.getPhoneNumber());

        MvcResult result4 = mockMvc.perform(delete(String.format("/user/%d", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //TODO: check what user delete

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



}
