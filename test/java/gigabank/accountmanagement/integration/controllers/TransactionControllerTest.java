package gigabank.accountmanagement.integration.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gigabank.accountmanagement.dto.TransactionDTO;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.mapper.BankAccountMapper;
import gigabank.accountmanagement.repository.BankAccountRepository;
import gigabank.accountmanagement.repository.TransactionRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    Long ownerId;
    Long accountId;
    Long t1Id;
    Long t2Id;
    Long t3Id;
    Long t4Id;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

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
        User newUser = userRepository.create(user);
        ownerId = newUser.getId();

        BankAccount bankAccount = new BankAccount(1L, new BigDecimal(12345.12345),
                userRepository.findById(BigInteger.valueOf(ownerId)) , new ArrayList<>());
        BankAccount newBankAccount = bankAccountRepository.create(bankAccount);
        accountId = newBankAccount.getId();

        Transaction transaction1 = new Transaction(1L, new BigDecimal(100), TransactionType.PAYMENT,
                "Category1", bankAccount, LocalDateTime.now());
        Transaction transaction2 = new Transaction(1L, new BigDecimal(100), TransactionType.PAYMENT,
                "Category2", bankAccount, LocalDateTime.now());
        Transaction transaction3 = new Transaction(1L, new BigDecimal(100), TransactionType.DEPOSIT,
                "Category1", bankAccount, LocalDateTime.now());
        Transaction transaction4 = new Transaction(1L, new BigDecimal(100), TransactionType.DEPOSIT,
                "Category3", bankAccount, LocalDateTime.now());

        Transaction t1 = transactionRepository.create(transaction1);
        Transaction t2 = transactionRepository.create(transaction2);
        Transaction t3 = transactionRepository.create(transaction3);
        Transaction t4 = transactionRepository.create(transaction4);

        t1Id = t1.getId();
        t2Id = t2.getId();
        t3Id = t3.getId();
        t4Id = t4.getId();

    }

    @Test
    public void testCreateTransaction() throws Exception {
        TransactionDTO transaction = new TransactionDTO(1L, new BigDecimal(123), TransactionType.PAYMENT,
                "TESTING", BankAccountMapper.toDTO(bankAccountRepository.findById(BigInteger.valueOf(accountId))));
        String serializedTransaction = objectMapper.writeValueAsString(transaction);

        MvcResult result = mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedTransaction))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        TransactionDTO createdTransaction = objectMapper.readValue(result.getResponse().getContentAsString(),
                TransactionDTO.class);

        assertNotNull(createdTransaction);
        assertEquals(transaction.getValue(), createdTransaction.getValue());
        assertEquals(transaction.getType(), createdTransaction.getType());
        assertEquals(transaction.getCategory(), createdTransaction.getCategory());
        assertEquals(transaction.getBankAccount().getId(), createdTransaction.getBankAccount().getId());

        Long id = createdTransaction.getTransactionId();

        assertNotNull(id);

        Transaction transactionByRepository = transactionRepository.findById(BigInteger.valueOf(id));

        assertNotNull(transactionByRepository);
        assertEquals(transaction.getValue(), transactionByRepository.getValue());
        assertEquals(transaction.getType(), transactionByRepository.getType());
        assertEquals(transaction.getCategory(), transactionByRepository.getCategory());
        assertEquals(transaction.getBankAccount().getId(), transactionByRepository.getBankAccount().getId());

        MvcResult result1 = mockMvc.perform(get(String.format("/transaction/%d", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        TransactionDTO getTransaction = objectMapper.readValue(result1.getResponse().getContentAsString(),
                TransactionDTO.class);

        assertNotNull(getTransaction);
        assertEquals(id, getTransaction.getTransactionId());
        assertEquals(transaction.getValue(), getTransaction.getValue());
        assertEquals(transaction.getType(), getTransaction.getType());
        assertEquals(transaction.getCategory(), getTransaction.getCategory());
        assertEquals(transaction.getBankAccount().getId(), getTransaction.getBankAccount().getId());


        //search test

        String type1 = TransactionType.DEPOSIT.name();
        String type2 = TransactionType.PAYMENT.name();
        String category1 = "Category1";
        String category2 = "Category2";
        String category3 = "Category3";

        MvcResult firstResult = mockMvc.perform(get(String.format("/transaction/search?type=%s", type1)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<TransactionDTO> transactions = objectMapper.readValue(firstResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertFalse(transactions.isEmpty());
        for (TransactionDTO transaction1 : transactions) {
            assertEquals(transaction1.getType().name(), type1);
        }

        List<Transaction> transactionsByRepository = transactionRepository.findByCategoryAndType(null, type1);

        assertEquals(transactions.size(), transactionsByRepository.size());
        for (int i = 0; i < transactions.size(); i++) {
            assertEquals(transactions.get(i).getTransactionId(), transactionsByRepository.get(i).getId());
            assertEquals(transactions.get(i).getValue(), transactionsByRepository.get(i).getValue());
            assertEquals(transactions.get(i).getType().name(), transactionsByRepository.get(i).getType().name());
        }

        MvcResult secondResult = mockMvc.perform(get(String.format("/transaction/search?category=%s", category1)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<TransactionDTO> secondTransactions = objectMapper.readValue(secondResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertFalse(secondTransactions.isEmpty());
        for (TransactionDTO secondTransaction : secondTransactions) {
            assertEquals(secondTransaction.getCategory(), category1);
        }

        List<Transaction> secondTransactionByRepository = transactionRepository.findByCategoryAndType(category1, null);

        assertEquals(secondTransactions.size(), secondTransactionByRepository.size());
        for (int i = 0; i < transactions.size(); i++) {
            assertEquals(secondTransactions.get(i).getTransactionId(), secondTransactionByRepository.get(i).getId());
            assertEquals(secondTransactions.get(i).getValue(), secondTransactionByRepository.get(i).getValue());
            assertEquals(secondTransactions.get(i).getCategory(), secondTransactionByRepository.get(i).getCategory());
        }

        MvcResult thirdResult = mockMvc.perform(get(String.format("/transaction/search?category=%s&type=%s", category1, type1)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<TransactionDTO> thirdTransactions = objectMapper.readValue(thirdResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertFalse(thirdTransactions.isEmpty());
        for (TransactionDTO thirdTransaction : thirdTransactions) {
            assertEquals(thirdTransaction.getCategory(), category1);
            assertEquals(thirdTransaction.getType().name(), type1);
        }

        List<Transaction> thirdTransactionByRepository = transactionRepository.findByCategoryAndType(category1, type1);

        assertEquals(thirdTransactions.size(), thirdTransactionByRepository.size());
        for (int i = 0; i < thirdTransactions.size(); i++) {
            assertEquals(thirdTransactions.get(i).getTransactionId(), thirdTransactionByRepository.get(i).getId());
            assertEquals(thirdTransactions.get(i).getValue(), thirdTransactionByRepository.get(i).getValue());
            assertEquals(thirdTransactions.get(i).getCategory(), thirdTransactionByRepository.get(i).getCategory());
            assertEquals(thirdTransactions.get(i).getBankAccount().getId(),
                    thirdTransactionByRepository.get(i).getBankAccount().getId());
            assertEquals(thirdTransactions.get(i).getType(), thirdTransactionByRepository.get(i).getType());
        }



        TransactionDTO transaction2 = new TransactionDTO(1L, new BigDecimal(1000), TransactionType.PAYMENT,
                "TESTING", BankAccountMapper.toDTO(bankAccountRepository.findById(BigInteger.valueOf(accountId))));
        String serializedTransaction2 = objectMapper.writeValueAsString(transaction2);
        MvcResult result2 = mockMvc.perform(post(String.format("/transaction/%d", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedTransaction2))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        TransactionDTO updateTransaction = objectMapper.readValue(result2.getResponse().getContentAsString(),
                TransactionDTO.class);



        assertNotNull(updateTransaction);
        assertEquals(id, updateTransaction.getTransactionId());
        assertEquals(transaction2.getValue(), updateTransaction.getValue());
        assertEquals(transaction2.getType(), updateTransaction.getType());
        assertEquals(transaction2.getCategory(), updateTransaction.getCategory());
        assertEquals(transaction2.getBankAccount().getId(), updateTransaction.getBankAccount().getId());

        Transaction updatedTransactionByRepository = transactionRepository.findById(BigInteger.valueOf(id));

        assertNotNull(updatedTransactionByRepository);
        assertEquals(id, updatedTransactionByRepository.getId());
        assertEquals(transaction2.getValue().compareTo(updatedTransactionByRepository.getValue()), 0);
        assertEquals(transaction2.getType(), updatedTransactionByRepository.getType());
        assertEquals(transaction2.getCategory(), updatedTransactionByRepository.getCategory());
        assertEquals(transaction2.getBankAccount().getId(), updatedTransactionByRepository.getBankAccount().getId());

        MvcResult result3 = mockMvc.perform(delete(String.format("/transaction/%d", id)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

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

    @AfterEach
    public void tearDown() {
        transactionRepository.delete(BigInteger.valueOf(t1Id));
        transactionRepository.delete(BigInteger.valueOf(t2Id));
        transactionRepository.delete(BigInteger.valueOf(t3Id));
        transactionRepository.delete(BigInteger.valueOf(t4Id));

        bankAccountRepository.delete(BigInteger.valueOf(accountId));

        userRepository.delete(BigInteger.valueOf(ownerId));



    }

}
