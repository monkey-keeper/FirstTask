package gigabank.accountmanagement.integration.service;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.BankAccountService;
import gigabank.accountmanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

@SpringBootTest
public class BankAccountServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;


    @Test
    public void createBankAccountTest() {
        User user = new User("1", "first", "middle", "last", LocalDate.now(), new ArrayList<>());
        User newUser = userService.create(user);
        BankAccount bankAccount = new BankAccount("1", new BigDecimal(1234.123),
                newUser, new ArrayList<>());
        BankAccount newBankAccount = bankAccountService.createBankAccount(bankAccount);
        assertEquals(bankAccount.getBalance(), newBankAccount.getBalance());
        assertEquals(bankAccount.getOwner().getId(), newBankAccount.getOwner().getId());
    }

    @Test
    public void findBankAccountTest() {
        assertNotNull(bankAccountService.getBankAccounts());
    }

    @Test
    public void findBankAccountByIdTest() {
        assertNotNull(bankAccountService.getBankAccount("6").getBalance()); //TODO: проблема со сравнение BigDecimal.
    }

    @Test
    public void updateBankAccountTest() {
        BankAccount newBankAccount = new BankAccount("1", new BigDecimal(1.00), userService.findById("2"), new ArrayList<>());
        BankAccount updateBankAccount = bankAccountService.updateBankAccount("6", newBankAccount);
        assertEquals(updateBankAccount.getBalance(), newBankAccount.getBalance());
        assertEquals(updateBankAccount.getOwner().getId(), newBankAccount.getOwner().getId());
    }

    @Test
    public void deleteBankAccountTest() {
        bankAccountService.deleteBankAccount("8");
        assertNull(bankAccountService.getBankAccount("8"));
    }

}
