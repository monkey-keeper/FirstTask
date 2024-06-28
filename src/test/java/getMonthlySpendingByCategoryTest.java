import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.AnalyticsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class getMonthlySpendingByCategoryTest {
    private static AnalyticsService analyticsService;
    private BankAccount bankAccount;
    private Transaction transaction1;
    private Transaction transaction2;
    private User user;
    private List<Transaction> transactions = new ArrayList<>();
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @Before
    public void initializeAnalyticsService() {
        analyticsService = new AnalyticsService();
        initializeUser();
        initializeBankAccount();
        initializeTransaction();
        addTransactionToTransactionsList();
        addBankAccountToBankAccountList();
    }

    public void initializeUser() {
        user = new User("2", "User", "MiddleName", "LastName",
                LocalDate.of(1997, 5, 20),
                bankAccounts);
    }


    public void initializeBankAccount() {
        bankAccount = new BankAccount("1", new BigDecimal(150),
                new User("2", "User", "MiddleName", "LastName",
                        LocalDate.of(1997, 5, 20),
                        bankAccounts), transactions);
    }


    public void initializeTransaction() {
        transaction1 = new Transaction("1", new BigDecimal(100), TransactionType.PAYMENT,
                "Health", bankAccount, LocalDateTime.of(2024, 6, 5, 14, 12));
        transaction2 = new Transaction("2", new BigDecimal(120), TransactionType.PAYMENT,
                "Health", bankAccount, LocalDateTime.of(2024, 6, 3, 14, 12));
    }


    public void addTransactionToTransactionsList() {
        bankAccount.getTransactions().add(transaction1);
        bankAccount.getTransactions().add(transaction2);
    }

    public void addBankAccountToBankAccountList() {
        bankAccount.getOwner().getBankAccounts().add(bankAccount);
    }


    /**
     * transaction.value = 100, 120
     * Складываем значения транзакций и получаем 220
     */
    @Test
    public void testShouldReturnSumOfTransactions() {
        Assert.assertEquals(new BigDecimal(220),
                analyticsService.getMonthlySpendingByCategory(bankAccount, "Health"));

        System.out.println(analyticsService.getMonthlySpendingByCategory(bankAccount, "Health"));
    }

    /**
     * Возвращает BigDecimal.ZERO, т.к bankAccount == null
     */
    @Test
    public void nullBankAccountTestShouldReturnBigDecimalZero() {
        Assert.assertEquals(BigDecimal.ZERO,
                analyticsService.getMonthlySpendingByCategory(null, "Health"));
    }

    /**
     * Возвращает BigDecimal.ZERO, т.к указана неверная категория транзакции
     */
    @Test
    public void incorrectCategoryNameTestShouldReturnBigDecimalZero() {
        Assert.assertEquals(BigDecimal.ZERO,
                analyticsService.getMonthlySpendingByCategory(bankAccount, "incorrect category"));
    }

}
