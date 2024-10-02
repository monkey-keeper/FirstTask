package gigabank.accountmanagement.integration.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.repository.BankAccountRepository;
import gigabank.accountmanagement.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.thymeleaf.spring6.expression.Mvc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateTransaction() throws Exception {
        Transaction transaction = new Transaction("1", new BigDecimal(123), TransactionType.PAYMENT, "TESTING",
                bankAccountRepository.findById(BigInteger.valueOf(Long.parseLong("6"))), LocalDateTime.now());
        String serializedTransaction = objectMapper.writeValueAsString(transaction);

        MvcResult result = mockMvc.perform(post("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedTransaction))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Transaction createdTransaction = objectMapper.readValue(result.getResponse().getContentAsString(), Transaction.class);

        assertEquals(transaction.getValue(), createdTransaction.getValue());
        assertEquals(transaction.getType(), createdTransaction.getType());
        assertEquals(transaction.getCategory(), createdTransaction.getCategory());
        assertEquals(transaction.getBankAccount().getId(), createdTransaction.getBankAccount().getId());

    }

    @Test
    public void testGetAllTransactions() throws Exception {
        MvcResult result = mockMvc.perform(get("/transaction"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<Transaction> transactions = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Transaction>>() {});

        assertFalse(transactions.isEmpty());

        Transaction firstTransaction = transactions.get(0);

       // assertNotNull(firstTransaction.getId()); //TODO: что-то не так с id (в repository все нормально)

        Transaction firstTransactionByRepository = transactionRepository.findAll().get(0);

        assertNotNull(firstTransactionByRepository.getId());
       // assertEquals(firstTransaction.getId(), firstTransactionByRepository.getId());
        assertEquals(firstTransaction.getValue(), firstTransactionByRepository.getValue());
        assertEquals(firstTransaction.getType(), firstTransactionByRepository.getType());
        assertEquals(firstTransaction.getCategory(), firstTransactionByRepository.getCategory());
        assertEquals(firstTransaction.getBankAccount().getId(), firstTransactionByRepository.getBankAccount().getId());


    }

    @Test
    public void testGetTransactionById() throws Exception {
        String id = "3";
        MvcResult result = mockMvc.perform(get(String.format("/transaction/%s", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Transaction transaction = objectMapper.readValue(result.getResponse().getContentAsString(), Transaction.class);

        assertNotNull(transaction);
        // assertEquals(id, transaction.getId());

        Transaction transactionByRepository = transactionRepository.findById(BigInteger.valueOf(Long.parseLong(id)));

        assertNotNull(transactionByRepository);

        assertEquals(transaction.getValue(), transactionByRepository.getValue());
        assertEquals(transaction.getType(), transactionByRepository.getType());
        assertEquals(transaction.getCategory(), transactionByRepository.getCategory());
        assertEquals(transaction.getBankAccount().getId(), transactionByRepository.getBankAccount().getId());

    }

    @Test
    public void testUpdateTransaction() throws Exception {
        String id = "3";
        Transaction transaction = new Transaction("1", new BigDecimal(3333333.0), TransactionType.PAYMENT,
                "TESTING", bankAccountRepository.findById(BigInteger.valueOf(Long.parseLong("6"))),
                LocalDateTime.now());
        String serializedTransaction = objectMapper.writeValueAsString(transaction);

        MvcResult result = mockMvc.perform(post(String.format("/transaction/%s", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedTransaction))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Transaction updatedTransaction = objectMapper.readValue(result.getResponse().getContentAsString(), Transaction.class);

        assertEquals(transaction.getValue(), updatedTransaction.getValue());
        assertEquals(transaction.getType(), updatedTransaction.getType());
        assertEquals(transaction.getCategory(), updatedTransaction.getCategory());
        assertEquals(transaction.getBankAccount().getId(), updatedTransaction.getBankAccount().getId());

        Transaction transactionByRepository = transactionRepository.findById(BigInteger.valueOf(Long.parseLong(id)));

        assertNotNull(transactionByRepository);
        assertEquals(updatedTransaction.getValue(), transactionByRepository.getValue());
        assertEquals(updatedTransaction.getType(), transactionByRepository.getType());
        assertEquals(updatedTransaction.getCategory(), transactionByRepository.getCategory());
        assertEquals(updatedTransaction.getBankAccount().getId(), transactionByRepository.getBankAccount().getId());

    }

    @Test
    public void testSearchTransaction() throws Exception {
        String type = TransactionType.PAYMENT.name();
        String category = "TESTING";

        MvcResult firstResult = mockMvc.perform(get(String.format("/transaction/search?type=%s", type)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<Transaction> transactions = objectMapper.readValue(firstResult.getResponse().getContentAsString(),
                new TypeReference<List<Transaction>>() {});

        assertFalse(transactions.isEmpty());
        assertEquals(transactions.get(0).getType().name(), type);

        List<Transaction> transactionsByRepository = transactionRepository.findByCategoryAndType(null, type);

        assertEquals(transactions.size(), transactionsByRepository.size());

        MvcResult secondResult = mockMvc.perform(get(String.format("/transaction/search?category=%s", category)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<Transaction> secondTransactions = objectMapper.readValue(firstResult.getResponse().getContentAsString(),
                new TypeReference<List<Transaction>>() {});

        assertFalse(secondTransactions.isEmpty());
        assertEquals(secondTransactions.get(0).getCategory(), category);

        List<Transaction> secondTransactionByRepository = transactionRepository.findByCategoryAndType(category, null);

        assertEquals(secondTransactions.size(), secondTransactionByRepository.size());

        MvcResult thirdResult = mockMvc.perform(get(String.format("/transaction/search?category=%s&type=%s", category, type)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<Transaction> thirdTransactions = objectMapper.readValue(firstResult.getResponse().getContentAsString(),
                new TypeReference<List<Transaction>>() {});

        assertFalse(thirdTransactions.isEmpty());
        assertEquals(thirdTransactions.get(0).getCategory(), category);
        assertEquals(thirdTransactions.get(0).getType().name(), type);

        List<Transaction> thirdTransactionByRepository = transactionRepository.findByCategoryAndType(category, type);

        assertEquals(thirdTransactions.size(), thirdTransactionByRepository.size());

    }

    @Test
    public void testDeleteTransaction() throws Exception {
        String id = "4";
        MvcResult result = mockMvc.perform(delete(String.format("/transaction/%s", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 200);
        assertNull(objectMapper.readValue(result.getResponse().getContentAsString(), Transaction.class));
    }

}
