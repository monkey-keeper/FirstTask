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
import java.util.LinkedHashMap;
import java.util.List;

public class TransactionByIdentificationTest {
    private static AnalyticsService analyticsService;
    private BankAccount bankAccount;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;
    private Transaction transaction4;
    private User user;
    private List<Transaction> transactions = new ArrayList<>();
    private List<BankAccount> bankAccounts = new ArrayList<>();

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
        user = new User("2", "User1", "MiddleName", "LastName",
                LocalDate.of(1997, 5, 20),
                bankAccounts);
    }


    public void initializeBankAccount() {
        bankAccount = new BankAccount("1", new BigDecimal(150),
                new User("2", "User1", "MiddleName", "LastName",
                        LocalDate.of(1997, 5, 20),
                        bankAccounts), transactions);
    }


    public void initializeTransaction() {
        transaction1 = new Transaction("1", new BigDecimal(100), TransactionType.PAYMENT,
                "Health", bankAccount, LocalDateTime.of(2024, 5, 27, 14, 12));
        transaction2 = new Transaction("2", new BigDecimal(120), TransactionType.PAYMENT,
                "Beauty", bankAccount, LocalDateTime.of(2024, 5, 30, 14, 12));
        transaction3 = new Transaction("3", new BigDecimal(160), TransactionType.PAYMENT,
                "Education", bankAccount, LocalDateTime.of(2024, 6, 3, 14, 12));
        transaction4 = new Transaction("4", new BigDecimal(110), TransactionType.PAYMENT,
                "Education", bankAccount, LocalDateTime.of(2024, 6, 5, 14, 12));
    }


    public void addTransactionToTransactionsList() {
        bankAccount.getTransactions().add(transaction1);
        bankAccount.getTransactions().add(transaction2);
        bankAccount.getTransactions().add(transaction3);
        bankAccount.getTransactions().add(transaction4);
    }

    public void addBankAccountToBankAccountList() {
        bankAccount.getOwner().getBankAccounts().add(bankAccount);
    }

    /**
     * Получаем список n транзакций
     */
    @Test
    public void getTransactionByIdentificationTest(){
        LinkedHashMap<LocalDateTime, Transaction> transactionListByIdentification =
                analyticsService.getTransactionListByIdentification(user, 4);
        System.out.println(transactionListByIdentification);

    }

    /**
     * Получаем пустой LinkedHashMap, т.к user == null
     */
    @Test
    public void nullUserTransactionShouldReturnNewLinkedHashMap(){
        LinkedHashMap<LocalDateTime, Transaction> transactionListByIdentification =
                analyticsService.getTransactionListByIdentification(null, 3);

        Assert.assertEquals(new LinkedHashMap<>(), transactionListByIdentification);
    }
}
