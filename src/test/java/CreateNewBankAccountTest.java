import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.AnalyticsService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateNewBankAccountTest {
    private static AnalyticsService analyticsService;
    private User user;
    private List<Transaction> transactions = new ArrayList<>();
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @Before
    public void initializeAnalyticsService() {
        analyticsService = new AnalyticsService();
        initializeUser();
    }

    public void initializeUser() {
        user = new User("2", "User", "MiddleName", "LastName",
                LocalDate.of(1997, 5, 20),
                bankAccounts);
    }


    /**
     * Создаём новый банковский аккаунт
     * Выводит информацию на консоль
     */
    @Test
    public void shouldCreateNewBankAccount(){
        analyticsService.createNewBankAccountForUser(user);
    }

    /**
     * Должны получить NullPointerException, т.к user == null
     */
    @Test(expected = NullPointerException.class)
    public void createNewBankAccountForNullUser(){
        analyticsService.createNewBankAccountForUser(null);
    }

}
