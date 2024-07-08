import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.AnalyticsService;
import gigabank.accountmanagement.service.TransactionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetMonthlySpendingTest {
    private static AnalyticsService analyticsService;
    private BankAccount bankAccount;
    private BankAccount bankAccount2;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;
    private Transaction transaction4;
    private Transaction transaction5;
    private User user;
    private User user2;
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
        addBankAccountToBankAccountList();
        addTransactionToTransactionsList();
    }

    public void initializeUser() {
        user = new User("2", "User", "MiddleName", "LastName",
                LocalDate.of(1997, 2, 10),
                bankAccounts);
        user2 = new User("3", "User2", "MiddleName2", "LastName2",
                LocalDate.of(1994, 1, 5),
                bankAccounts2);
    }


    public void initializeBankAccount() {
        bankAccount = new BankAccount("1", new BigDecimal(150),
                user, transactions);
        bankAccount2 = new BankAccount("2", new BigDecimal(150),
                user2, transactions2);
    }


    public void initializeTransaction() {
        transaction1 = new Transaction("1", new BigDecimal(100), TransactionType.PAYMENT,
                "Health", bankAccount, LocalDateTime.of(2024, 6, 12, 14, 12));

        transaction2 = new Transaction("2", new BigDecimal(120), TransactionType.PAYMENT,
                "Beauty", bankAccount, LocalDateTime.of(2024, 6, 15, 14, 12));

        transaction3 = new Transaction("3", new BigDecimal(160), TransactionType.PAYMENT,
                "Education", bankAccount, LocalDateTime.of(2024, 6, 10, 14, 12));

        transaction4 = new Transaction("4", new BigDecimal(110), TransactionType.PAYMENT,
                "Education", bankAccount, LocalDateTime.of(2024, 6, 9, 14, 12));

        transaction5 = new Transaction("4", new BigDecimal(110), TransactionType.DEPOSIT,
                "Incorrect", bankAccount, LocalDateTime.of(2024, 6, 5, 14, 12));
    }


    public void addTransactionToTransactionsList() {
        bankAccount.getTransactions().add(transaction1);
        bankAccount.getTransactions().add(transaction2);
        bankAccount.getTransactions().add(transaction3);
        bankAccount.getTransactions().add(transaction4);

        bankAccount2.getTransactions().add(transaction5);
    }

    public void addBankAccountToBankAccountList() {
        bankAccount.getOwner().getBankAccounts().add(bankAccount);
        bankAccount2.getOwner().getBankAccounts().add(bankAccount2);
    }

    /**
     * Суммируем количество трат по категориям
     */
    @Test
    public void shouldReturnMonthlySpendingByCategories() {
        Map<String, BigDecimal> monthlySpendingByCategories = analyticsService.getMonthlySpendingByCategories(user,
                TransactionService.transactionCategories);

        System.out.println(monthlySpendingByCategories);
    }

    /**
     * Здесь у user2 указан неверный тип транзакции, должно вернуть пустую мапу
     */
    @Test
    public void noTransactionTypePaymentShouldReturnEmptyHashMap() {
        Map<String, BigDecimal> monthlySpendingByCategories = analyticsService.getMonthlySpendingByCategories(user2,
                TransactionService.transactionCategories);

        Assert.assertEquals(new HashMap<>(), monthlySpendingByCategories);
    }

    /**
     * Здесь у user2 указана неверная категория транзакции, должно вернуть пустую мапу
     */
    @Test
    public void incorrectCategoryTypeShouldReturnEmptyHashMap() {
        Map<String, BigDecimal> monthlySpendingByCategories = analyticsService.getMonthlySpendingByCategories(user2,
                TransactionService.transactionCategories);

        Assert.assertEquals(new HashMap<>(), monthlySpendingByCategories);
    }
}
