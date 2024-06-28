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

public class paymentFromAndToAccountTest {
    private static AnalyticsService analyticsService;
    private BankAccount bankAccount;
    private BankAccount bankAccount2;
    private User user;
    private Transaction transaction;
    private Transaction transaction2;
    private Transaction transaction3;
    private List<Transaction> transactions = new ArrayList<>();
    private List<Transaction> transactions2 = new ArrayList<>();
    private List<BankAccount> bankAccounts = new ArrayList<>();
    private List<BankAccount> bankAccounts2 = new ArrayList<>();

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

        transaction2 = new Transaction("1", new BigDecimal(400), TransactionType.DEPOSIT,
                "Health", bankAccount, LocalDateTime.of(2024, 5, 29, 14, 12));

        transaction3 = new Transaction("1", new BigDecimal(0), TransactionType.DEPOSIT,
                "Health", bankAccount, LocalDateTime.of(2024, 5, 29, 14, 12));
    }

    public void initializeBankAccount() {
        bankAccount = new BankAccount("1", new BigDecimal(450),
                new User("2", "User", "MiddleName", "LastName",
                        LocalDate.of(1997, 5, 20),
                        bankAccounts), transactions);

        bankAccount2 = new BankAccount("2", new BigDecimal(320),
                new User("2", "User", "MiddleName", "LastName",
                        LocalDate.of(1991, 4, 11),
                        bankAccounts2), transactions2);
    }

    public void initializeUser() {
        user = new User("2", "User", "MiddleName", "LastName",
                LocalDate.of(1997, 5, 20),
                bankAccounts);
    }

    /**
     * Совершаем перевод денежных средств
     * В консоль выводит начальный и конечный баланс пользователя + список транзакций, которые были добавлены
     */
    @Test
    public void shouldPaymentTransferFromAndToAccount(){
        analyticsService.paymentFromAndToAccount(bankAccount, bankAccount2, new BigDecimal(15));
    }

    @Test(expected = NullPointerException.class)
    public void paymentTransferFromAndToAccountWithNullAccount(){
        analyticsService.paymentFromAndToAccount(null, bankAccount2, new BigDecimal(15));
    }
}
