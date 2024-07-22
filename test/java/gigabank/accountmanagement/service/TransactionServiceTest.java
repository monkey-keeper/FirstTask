package gigabank.accountmanagement.service;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {
    private TransactionService transactionService;
    private User user;
    private BankAccount bankAccount1;
    private BankAccount bankAccount2;
    private BankAccount bankAccount3;

    private static final BigDecimal TEN_DOLLARS = new BigDecimal("10.00");
    private static final BigDecimal FIFTEEN_DOLLARS = new BigDecimal("15.00");
    private static final BigDecimal TWENTY_DOLLARS = new BigDecimal("20.00");

    private static final String BEAUTY_CATEGORY = "Beauty";
    private static final String FOOD_CATEGORY = "Food";
    private static final String EDUCATION_CATEGORY = "Education";

    private static final LocalDateTime TEN_DAYS_AGO = LocalDateTime.now().minusDays(10L);
    private static final LocalDateTime FIVE_MONTHS_AGO = LocalDateTime.now().minusMonths(5L);
    private static final LocalDateTime THREE_DAYS_AGO = LocalDateTime.now().minusDays(3L);
    private static final LocalDateTime ONE_DAY_AGO = LocalDateTime.now().minusDays(1L);
    private static final LocalDateTime FIVE_WEEKS_AGO = LocalDateTime.now().minusWeeks(5L);
    private static final LocalDateTime ONE_MONTH_AGO = LocalDateTime.now().minusMonths(1L);


    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService();
        user = new User();
        bankAccount1 = new BankAccount();
        bankAccount2 = new BankAccount();
        bankAccount3 = new BankAccount();

        bankAccount1.getTransactions().add(new Transaction("1", TEN_DOLLARS, TransactionType.PAYMENT, FOOD_CATEGORY, ONE_DAY_AGO));
        bankAccount1.getTransactions().add(new Transaction("2", FIFTEEN_DOLLARS, TransactionType.PAYMENT, BEAUTY_CATEGORY, TEN_DAYS_AGO));
        bankAccount1.getTransactions().add(new Transaction("3", TWENTY_DOLLARS, TransactionType.PAYMENT, EDUCATION_CATEGORY, THREE_DAYS_AGO));

        bankAccount2.getTransactions().add(new Transaction("4", TEN_DOLLARS, TransactionType.PAYMENT, FOOD_CATEGORY, FIVE_WEEKS_AGO));
        bankAccount2.getTransactions().add(new Transaction("5", TWENTY_DOLLARS, TransactionType.PAYMENT, FOOD_CATEGORY, THREE_DAYS_AGO));
        bankAccount2.getTransactions().add(new Transaction("6", FIFTEEN_DOLLARS, TransactionType.PAYMENT, FOOD_CATEGORY, FIVE_MONTHS_AGO));

        bankAccount3.getTransactions().add(new Transaction("7", FIFTEEN_DOLLARS, TransactionType.DEPOSIT, FOOD_CATEGORY, ONE_DAY_AGO));
        bankAccount3.getTransactions().add(new Transaction("8", TWENTY_DOLLARS, TransactionType.DEPOSIT, BEAUTY_CATEGORY, TEN_DAYS_AGO));
        bankAccount3.getTransactions().add(new Transaction("9", FIFTEEN_DOLLARS, TransactionType.DEPOSIT, EDUCATION_CATEGORY, THREE_DAYS_AGO));

        user.getBankAccounts().add(bankAccount1);
        user.getBankAccounts().add(bankAccount2);
        user.getBankAccounts().add(bankAccount3);

    }


    @Test
    void filterTransactions() {
        Predicate<Transaction> isOneMonthAgo = transaction -> transaction.getCreatedDate().isAfter(ONE_MONTH_AGO);
        List<Transaction> result = transactionService.filterTransactions(user, isOneMonthAgo);
        assertEquals(7, result.size());

        List<Transaction> result1 = transactionService.filterTransactions(null, isOneMonthAgo);
        assertEquals(0, result1.size());

        Predicate<Transaction> isPayment = transaction -> TransactionType.PAYMENT.equals(transaction.getType());
        List<Transaction> result3 = transactionService.filterTransactions(user, isPayment);
        assertEquals(6, result3.size());
    }

    @Test
    void transformTransaction() {

    }

    @Test
    void processTransactions() {

    }

    @Test
    void createTrasactionList() {

    }

    @Test
    void mergeTransactionList() {

    }
}