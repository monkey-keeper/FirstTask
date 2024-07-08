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

public class DeleteBankAccountTest {
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
        addBankAccountToBankAccountList();
    }

    public void initializeBankAccount() {
        bankAccount = new BankAccount("1", new BigDecimal(150),
                new User("2", "User", "MiddleName", "LastName",
                        LocalDate.of(1997, 5, 20),
                        bankAccounts), transactions);
    }


    public void initializeUser() {
        user = new User("2", "User", "MiddleName", "LastName",
                LocalDate.of(1997, 5, 20),
                bankAccounts);
    }
    public void addBankAccountToBankAccountList() {
        bankAccount.getOwner().getBankAccounts().add(bankAccount);
    }

    /**
     * Удаляет банковский аккаунт
     * Выводит информацию на консоль
     */
    @Test
    public void shouldDeleteBankAccount(){
        analyticsService.deleteUserBankAccount(user, bankAccount);
    }

    /**
     * Получаем исключение NullPointerException и информацию о том, что пользователь не был найден
     */
    @Test(expected = NullPointerException.class)
    public void shouldDeleteBankAccountWithNullUser(){
        analyticsService.deleteUserBankAccount(null, bankAccount);
    }
}
