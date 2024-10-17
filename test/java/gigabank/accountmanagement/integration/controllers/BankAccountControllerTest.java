package gigabank.accountmanagement.integration.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.mapper.UserMapper;
import gigabank.accountmanagement.repository.BankAccountRepository;
import gigabank.accountmanagement.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountControllerTest {
    Long ownerId;
    Long accountId;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        User user = new User(1L, "F1", "M1", "L1",
                LocalDate.now().minusYears(20), new ArrayList<>(), "1234567890");
        User newUser = userRepository.save(user);
        ownerId = newUser.getId();
        BankAccount bankAccount = new BankAccount(1L, new BigDecimal(12345.12345),
                userRepository.findById(ownerId).get() , new ArrayList<>());
        BankAccount newBankAccount = bankAccountRepository.save(bankAccount);
        accountId = newBankAccount.getId();
    }

    @Test
    public void testCreateBankAccount() throws Exception {
        BankAccountDTO bankAccount = new BankAccountDTO(1L, new BigDecimal("1234.12"),
                UserMapper.toDTO(userRepository.findById(ownerId).get()));
        String serializedBankAccount = objectMapper.writeValueAsString(bankAccount);

        MvcResult result = mockMvc.perform(post("/bank-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedBankAccount))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        BankAccountDTO savedBankAccount = objectMapper.readValue(result.getResponse().getContentAsString(),
                BankAccountDTO.class);

        assertEquals(bankAccount.getBalance(), savedBankAccount.getBalance());
        assertEquals(bankAccount.getOwner().getId(), savedBankAccount.getOwner().getId());

        Long id = savedBankAccount.getId();

        assertNotNull(id);

        BankAccount bankAccountByRepository = bankAccountRepository.findById(id).get();

        assertNotNull(bankAccountByRepository);
        assertEquals(bankAccount.getBalance().compareTo(bankAccountByRepository.getBalance()), 0);
        assertEquals(bankAccount.getOwner().getId(), bankAccountByRepository.getOwner().getId());

        MvcResult result1 = mockMvc.perform(get(String.format("/bank-account/%d", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BankAccountDTO getBankAccount = objectMapper.readValue(result1.getResponse().getContentAsString(),
                BankAccountDTO.class);

        assertNotNull(getBankAccount);
        assertEquals(id, getBankAccount.getId());
        assertEquals(bankAccount.getBalance().compareTo(getBankAccount.getBalance()), 0);
        assertEquals(bankAccount.getOwner().getId(), getBankAccount.getOwner().getId());

        BankAccountDTO bankAccount2 = new BankAccountDTO(1L, new BigDecimal("1.0"),
                UserMapper.toDTO(userRepository.findById(ownerId).get()));
        String serializedBankAccount2 = objectMapper.writeValueAsString(bankAccount2);
        MvcResult result2 = mockMvc.perform(post(String.format("/bank-account/%d", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedBankAccount2))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BankAccountDTO updateBankAccount = objectMapper.readValue(result2.getResponse().getContentAsString(),
                BankAccountDTO.class);

        assertNotNull(updateBankAccount);
        assertEquals(id, updateBankAccount.getId());
        assertEquals(bankAccount2.getBalance().compareTo(updateBankAccount.getBalance()), 0);
        assertEquals(bankAccount2.getOwner().getId(), updateBankAccount.getOwner().getId());

        BankAccount updateBankAccountByRepository = bankAccountRepository.findById(id).get();

        assertNotNull(updateBankAccountByRepository);
        assertEquals(id, updateBankAccountByRepository.getId());
        assertEquals(bankAccount2.getBalance().compareTo(updateBankAccountByRepository.getBalance()), 0);
        assertEquals(bankAccount2.getOwner().getId(), updateBankAccountByRepository.getOwner().getId());

        MvcResult result3 = mockMvc.perform(delete(String.format("/bank-account/%d", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

    }

    @Test
    public void testGetAllBankAccount() throws Exception {
        MvcResult result = mockMvc.perform(get("/bank-account"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<BankAccount> BankAccounts = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<BankAccount>>() {});
        assertFalse(BankAccounts.isEmpty());

        BankAccount bankAccount = BankAccounts.stream()
                .filter(ba -> ba.getId().equals(accountId))
                .findFirst()
                .orElse(null);

        assertNotNull(bankAccount.getId());

        BankAccount bankAccountByRepository = bankAccountRepository.findAll().stream()
                .filter(ba -> ba.getId().equals(accountId))
                .findFirst()
                .orElse(null);

        assertEquals(bankAccount.getBalance(), bankAccountByRepository.getBalance());
        assertEquals(bankAccount.getOwner().getId(), bankAccountByRepository.getOwner().getId());

    }

    @AfterEach
    public void tearDown() {
        bankAccountRepository.deleteById(accountId);;
        userRepository.deleteById(ownerId);
    }


}
