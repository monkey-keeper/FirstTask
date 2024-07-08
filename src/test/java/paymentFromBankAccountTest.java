import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.AnalyticsService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class paymentFromBankAccountTest {
    private static AnalyticsService analyticsService;
    private BankAccount bankAccount;
    private User user;
    private Transaction transaction;
    private Transaction transaction2;
    private Transaction transaction3;
    private List<Transaction> transactions = new ArrayList<>();
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @Before
    public void initializeAnalyticsService() {
        analyticsService = new AnalyticsService();
        initializeUser();
        initializeBankAccount();
        initializeTransaction();
    }
    public void initializeTransaction() {
        transaction = new Transaction("1", new BigDecimal(120), TransactionType.DEPOSIT,
                "Health", bankAccount, LocalDateTime.of(2024, 5, 29, 14, 12));

        transaction2 = new Transaction("1", new BigDecimal(500), TransactionType.DEPOSIT,
                "Health", bankAccount, LocalDateTime.of(2024, 5, 29, 14, 12));

        transaction3 = null;
    }

    public void initializeBankAccount() {
        bankAccount = new BankAccount("1", new BigDecimal(200),
                new User("2", "User", "MiddleName", "LastName",
                        LocalDate.of(1997, 5, 20),
                        bankAccounts), transactions);
    }

    public void initializeUser() {
        user = new User("2", "User", "MiddleName", "LastName",
                LocalDate.of(1997, 5, 20),
                bankAccounts);
    }

    /**
     * Начальный баланс 200, делаем оплату на сумму 120, результат 80
     */
    @Test
    public void shouldPaymentFromBankAccount(){
        analyticsService.paymentFromBankAccount(bankAccount, transaction);
    }

    /**
     * Должно вывести сообщение - Insufficient funds in the account
     * Недостаточно средств на балансе
     */
    @Test
    public void paymentWithInsufficientAmount(){
        analyticsService.paymentFromBankAccount(bankAccount, transaction2);
    }

    /**
     * Здесь передаём null транзакцию и должны получить исключение NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void paymentWithNullBankAccount(){
        analyticsService.paymentFromBankAccount(bankAccount, transaction3);
    }
}
