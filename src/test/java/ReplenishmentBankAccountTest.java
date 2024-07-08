import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.AnalyticsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReplenishmentBankAccountTest {
    private static AnalyticsService analyticsService;
    private BankAccount bankAccount;
    private User user;
    private List<Transaction> transactions = new ArrayList<>();
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @Before
    public void initializeAnalyticsService() {
        analyticsService = new AnalyticsService();
        initializeUser();
        initializeBankAccount();
    }

    public void initializeBankAccount() {
        bankAccount = new BankAccount("1", new BigDecimal(0),
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
     * Баланс аккаунта = 0, пополняем его на переданное значение (10)
     */
    @Test
    public void shouldReplenishmentBankAccount(){
        analyticsService.replenishmentBankAccount(bankAccount, BigDecimal.TEN);
    }

    /**
     * Банк аккаунт равен null, получаем исключение NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void nullBankAccountShouldReturnNullPointerException(){
        analyticsService.replenishmentBankAccount(null, BigDecimal.TEN);
    }
}
