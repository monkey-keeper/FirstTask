package gigabank.accountmanagement.integration.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.repository.BankAccountRepository;
import gigabank.accountmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateBankAccount() throws Exception {
        BankAccount bankAccount = new BankAccount("1", new BigDecimal(1234.123),
                userRepository.findById(BigInteger.valueOf(Long.parseLong("4"))), new ArrayList<>());
        String serializedBankAccount = objectMapper.writeValueAsString(bankAccount);

        MvcResult result = mockMvc.perform(post("/bank-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedBankAccount))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        BankAccount savedBankAccount = objectMapper.readValue(result.getResponse().getContentAsString(), BankAccount.class);

        assertEquals(bankAccount.getBalance(), savedBankAccount.getBalance());
        assertEquals(bankAccount.getOwner().getId(), savedBankAccount.getOwner().getId());

    }

    @Test
    public void testGetAllBankAccount() throws Exception {
        MvcResult result = mockMvc.perform(get("/bank-account"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<BankAccount> BankAccounts = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<BankAccount>>() {});
        assertFalse(BankAccounts.isEmpty());

        BankAccount bankAccount = BankAccounts.get(0);

        assertNotNull(bankAccount.getId());

        BankAccount bankAccountByRepository = bankAccountRepository.findAll().get(0);

        assertEquals(bankAccount.getBalance(), bankAccountByRepository.getBalance());
        assertEquals(bankAccount.getOwner().getId(), bankAccountByRepository.getOwner().getId());

    }

    @Test
    public void testGetBankAccountById() throws Exception {
        String id = "6";
        MvcResult result = mockMvc.perform(get(String.format("/bank-account/%s", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BankAccount bankAccount = objectMapper.readValue(result.getResponse().getContentAsString(), BankAccount.class);

        assertNotNull(bankAccount.getId());

        BankAccount bankAccountByRepository = bankAccountRepository.findById(BigInteger.valueOf(Long.parseLong(id)));

        assertEquals(bankAccount.getBalance(), bankAccountByRepository.getBalance());
        assertEquals(bankAccount.getOwner().getId(), bankAccountByRepository.getOwner().getId());

    }

    @Test
    public void testUpdateBankAccount() throws Exception {
        String id = "6";
        BankAccount bankAccount = new BankAccount("1", new BigDecimal(1),
                userRepository.findById(BigInteger.valueOf(Long.parseLong("4"))), new ArrayList<>());
        String serializedBankAccount = objectMapper.writeValueAsString(bankAccount);
        MvcResult result = mockMvc.perform(post(String.format("/bank-account/%s", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedBankAccount))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BankAccount updatedBankAccount = objectMapper.readValue(result.getResponse().getContentAsString(), BankAccount.class);

        assertEquals(updatedBankAccount.getBalance(), bankAccount.getBalance());
        assertEquals(updatedBankAccount.getOwner().getId(), bankAccount.getOwner().getId());

        BankAccount bankAccountByRepository = bankAccountRepository.findById(BigInteger.valueOf(Long.parseLong(id)));

        assertEquals(updatedBankAccount.getBalance(), bankAccountByRepository.getBalance());
        assertEquals(updatedBankAccount.getOwner().getId(), bankAccountByRepository.getOwner().getId());
    }

    @Test
    public void testDeleteBankAccount() throws Exception {
        String id = "6";
        MvcResult result = mockMvc.perform(delete(String.format("/bank-account/%s", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertNull(objectMapper.readValue(result.getResponse().getContentAsString(), BankAccount.class));
    }

}
