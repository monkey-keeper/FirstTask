package gigabank.accountmanagement.integration.service;

import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.service.BankAccountService;
import gigabank.accountmanagement.service.TransactionService;
import gigabank.accountmanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Test
    public void createTransactionTest() {
        Transaction transaction = new Transaction(1L, new BigDecimal(10.0), TransactionType.PAYMENT, "",
                bankAccountService.getBankAccount("6"), LocalDateTime.now());
        Transaction newTransaction = transactionService.create(transaction);
        assertEquals(transaction.getValue(), newTransaction.getValue());
        assertEquals(transaction.getType(), newTransaction.getType());
        assertEquals(transaction.getCategory(), newTransaction.getCategory());
        assertEquals(transaction.getBankAccount().getId(), newTransaction.getBankAccount().getId());
        assertEquals(transaction.getCreatedDate(), newTransaction.getCreatedDate());
    }

    @Test
    public void findTransactionTest() {
        List<Transaction> transactions = transactionService.findTransaction();// тут id есть!
        assertFalse(transactionService.findTransaction().isEmpty());
    }

    @Test
    public void findByIdTransactionTest() {
        assertNotNull(transactionService.findById(1L));
    }

    @Test
    public void findByTransactionTest() {
        assertNotNull(transactionService.findTransaction("CREATE", TransactionType.PAYMENT.name()));
    }

    @Test
    public void updateTransactionTest() {
        Transaction transaction = new Transaction(1L, new BigDecimal(100.0), TransactionType.PAYMENT,
                "CREATE", bankAccountService.getBankAccount(""), LocalDateTime.now());
        Transaction newTransaction = transactionService.update(1L, transaction);
        assertEquals(transaction.getValue(), newTransaction.getValue());
        assertEquals(transaction.getType(), newTransaction.getType());
        assertEquals(transaction.getCategory(), newTransaction.getCategory());
        assertEquals(transaction.getBankAccount().getId(), newTransaction.getBankAccount().getId());
        assertEquals(transaction.getCreatedDate(), newTransaction.getCreatedDate());
    }

    @Test
    public void deleteTransactionTest() {
        transactionService.delete(1L);
        assertNull(transactionService.findById(1L));
    }

}
