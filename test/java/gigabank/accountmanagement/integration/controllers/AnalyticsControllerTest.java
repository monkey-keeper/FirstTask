package gigabank.accountmanagement.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gigabank.accountmanagement.dto.TransactionDTO;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.repository.BankAccountRepository;
import gigabank.accountmanagement.repository.TransactionRepository;
import gigabank.accountmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AnalyticsControllerTest {
    Long ownerId;
    Long accountId;
    Long t1Id;
    Long t2Id;
    Long t3Id;
    Long t4Id;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @BeforeEach
    void setUp() {
        User user = new User(1L, "F1", "M1", "L1",
                LocalDate.now().minusYears(20), new ArrayList<>(), "1234567890");
        User newUser = userRepository.save(user);
        ownerId = newUser.getId();

        BankAccount bankAccount = new BankAccount(1L, new BigDecimal(12345.12345),
                userRepository.findById(ownerId).get() , new ArrayList<>());
        BankAccount newBankAccount = bankAccountRepository.save(bankAccount);
        accountId = newBankAccount.getId();

        Transaction transaction1 = new Transaction(1L, new BigDecimal("100"), TransactionType.PAYMENT,
                "Category1", newBankAccount, LocalDateTime.now());
        Transaction transaction2 = new Transaction(1L, new BigDecimal("1111"), TransactionType.PAYMENT,
                "Category2", newBankAccount, LocalDateTime.now().minusYears(2));
        Transaction transaction3 = new Transaction(1L, new BigDecimal("1"), TransactionType.DEPOSIT,
                "Category1", newBankAccount, LocalDateTime.now());
        Transaction transaction4 = new Transaction(1L, new BigDecimal("100"), TransactionType.DEPOSIT,
                "Category3", newBankAccount, LocalDateTime.now());

        Transaction t1 = transactionRepository.save(transaction1);
        Transaction t2 = transactionRepository.save(transaction2);
        Transaction t3 = transactionRepository.save(transaction3);
        Transaction t4 = transactionRepository.save(transaction4);

        t1Id = t1.getId();
        t2Id = t2.getId();
        t3Id = t3.getId();
        t4Id = t4.getId();
    }

    @Test
    void getLargestTransactionFromBankAccount() throws Exception {

        // getLargestTransactionFromBankAccount
        MvcResult result = mockMvc.perform(get("/analytics/largest-transactions-from-bank-account/{accountId}", accountId))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        TransactionDTO transactionDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                TransactionDTO.class);

        assertNotNull(transactionDTO);
        assertEquals(transactionDTO.getValue().compareTo(new BigDecimal("1111")), 0);

        // getSmallestTransactionFromBankAccount

        MvcResult result1 = mockMvc.perform(get("/analytics/smallest-transaction-from-bank-account/{accountId}", accountId))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        TransactionDTO transactionDTO1 = objectMapper.readValue(result1.getResponse().getContentAsString(),
                TransactionDTO.class);

        assertNotNull(transactionDTO1);
        assertEquals(transactionDTO1.getValue().compareTo(new BigDecimal("1")), 0);

        // getAverageTransactionFromBankAccount
//
//        MvcResult result2 = mockMvc.perform(get("/analytics/average-transaction-from-bank-account/{accountId}", accountId))
//                .andExpect(status().is2xxSuccessful())
//                .andReturn();
//        TransactionDTO transactionDTO2 = objectMapper.readValue(result2.getResponse().getContentAsString(),
//                TransactionDTO.class);
//
//        assertNotNull(transactionDTO2);

        // getSumTransactionByCategoryFromBankAccount

        String category = "Category1";

        MvcResult result3 = mockMvc.perform(get("/analytics/sum-transaction-by-category-from-bank-account/{accountId}", accountId)
                .param("category", category))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BigDecimal getSumByCategory = objectMapper.readValue(result3.getResponse().getContentAsString(), BigDecimal.class);

        assertNotNull(getSumByCategory);
        assertEquals(getSumByCategory.compareTo(new BigDecimal("101")), 0);

        // getSumTransactionByDateRangeFromBankAccount

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusYears(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String startTimeStr = startTime.format(formatter);
        String endTimeStr = endTime.format(formatter);

        MvcResult result4 = mockMvc.perform(get("/analytics/sum-transaction-by-date-range-from-bank-account/{accountId}", accountId)
                .param("startDate", startTimeStr)
                .param("endDate", endTimeStr))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BigDecimal getSumByDate = objectMapper.readValue(result4.getResponse().getContentAsString(), BigDecimal.class);

        assertNotNull(getSumByDate);
        assertEquals(getSumByDate.compareTo(new BigDecimal("201")), 0);

    }


    @BeforeEach
    void tearDown() {
        transactionRepository.deleteAll();
        bankAccountRepository.deleteAll();
        userRepository.deleteAll();
    }

}
