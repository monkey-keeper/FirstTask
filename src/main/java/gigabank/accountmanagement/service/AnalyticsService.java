package gigabank.accountmanagement.service;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.User;
import org.w3c.dom.ls.LSOutput;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static gigabank.accountmanagement.entity.TransactionType.*;

/**
 * Сервис предоставляет аналитику по операциям пользователей
 */
public class AnalyticsService {
    private LocalDateTime now = LocalDateTime.now();
    private LocalDateTime minusMonth = now.minus(1L, ChronoUnit.MONTHS);
    private Random random = new Random();

    /**
     * Вывод суммы потраченных средств на категорию за последний месяц
     *
     * @param bankAccount - счет
     * @param category    - категория
     */
    public BigDecimal getMonthlySpendingByCategory(BankAccount bankAccount, String category) {
        BigDecimal sum = BigDecimal.ZERO;

        if (bankAccount == null) {
            return BigDecimal.ZERO;
        }

        for (Transaction transaction : bankAccount.getTransactions()) {

            if (transaction.getCategory() != category) {
                return BigDecimal.ZERO;
            }

            if (transaction.getCategory() == category && transaction.getType() == PAYMENT
                && transaction.getCreatedDate().isAfter(minusMonth) && transaction.getCreatedDate().isBefore(now)) {
                sum = sum.add(transaction.getValue());
            }
        }

        return sum;
    }

    /**
     * Вывод суммы потраченных средств на n категорий за последний месяц
     * со всех счетов пользователя
     *
     * @param user       - пользователь
     * @param categories - категории
     * @return мапа категория - сумма потраченных средств
     */
    public Map<String, BigDecimal> getMonthlySpendingByCategories(User user, Set<String> categories) {
        Map<String, BigDecimal> categorySum = new HashMap<>();

        if (user == null) {
            return new HashMap<>();
        }

        for (String category : categories) {
            categorySum.put(category, BigDecimal.ZERO);
        }

        for (BankAccount bankAccount : user.getBankAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                if (transaction.getType() == PAYMENT &&
                    transaction.getCreatedDate().isAfter(minusMonth) &&
                    transaction.getCreatedDate().isBefore(now)) {

                    String category = transaction.getCategory();

                    if (categories.contains(category)) {
                        BigDecimal currentSum = categorySum.get(category);
                        BigDecimal updateSum = currentSum.add(transaction.getValue());

                        categorySum.put(category, updateSum);
                    } else {
                        return new HashMap<>();
                    }
                } else {
                    return new HashMap<>();
                }
            }
        }

        return categorySum;
    }

    /**
     * Вывод платежных операций по всем счетам и по всем категориям от наибольшей к наименьшей
     *
     * @param user - пользователь
     * @return мапа категория - все операции совершенные по ней
     */
    public TreeMap<String, List<Transaction>> getTransactionHistorySortedByAmount(User user) {

        TreeMap<String, List<Transaction>> result = new TreeMap<>();
        List<Transaction> transactionList = new ArrayList<>();

        if (user == null) {
            return new TreeMap<>();
        }

        for (BankAccount bankAccount : user.getBankAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                if (transaction.getType() == PAYMENT) {
                    transactionList.add(transaction);
                    result.put(transaction.getCategory(), new ArrayList<>());
                }
            }
        }
        transactionList.sort(Comparator.comparing(Transaction::getValue).reversed());

        for (Map.Entry<String, List<Transaction>> entry : result.entrySet()) {
            List<Transaction> value = entry.getValue();
            String key = entry.getKey();
            for (Transaction transaction : transactionList) {
                if (key == transaction.getCategory()) {
                    value.add(transaction);
                }
            }
        }


        return result;
    }

    public LinkedHashMap<LocalDateTime, Transaction> getTransactionListByIdentification(User user, int num) {
        LinkedHashMap<LocalDateTime, Transaction> result = new LinkedHashMap<>();
        List<Transaction> transactionTime = new ArrayList<>();

        if (user == null) {
            return new LinkedHashMap<>();
        }

        for (BankAccount bankAccount : user.getBankAccounts()) {
            for (Transaction transaction : bankAccount.getTransactions()) {
                transactionTime.add(transaction);
            }
        }

        transactionTime.sort(Comparator.comparing(transaction -> transaction.getCreatedDate().getDayOfMonth(),
                Comparator.reverseOrder()));


        for (int i = 0; i < num; i++) {
            result.put(transactionTime.get(i).getCreatedDate(), transactionTime.get(i));
        }


        return result;
    }

    public PriorityQueue<Transaction> getLargestUserTransaction(User user, int num) {
        PriorityQueue<Transaction> result = new PriorityQueue<>(Comparator.comparing(transaction -> transaction.getValue(),
                Comparator.reverseOrder()));
        List<Transaction> transactionsList = new ArrayList<>();

            for (BankAccount bankAccount : user.getBankAccounts()) {
                for (Transaction transaction : bankAccount.getTransactions()) {
                    if(!bankAccount.getTransactions().isEmpty() && bankAccount.getTransactions().size() >= num){
                        if (transaction.getType() == PAYMENT) {
                            transactionsList.add(transaction);
                        }else{
                            System.out.println("Incorrect transaction type");
                        }
                    }else{
                        System.out.println("Нет транзакций за указанный период!");
                    }
                }
            }


        transactionsList.sort(Comparator.comparing(Transaction::getValue).reversed());


        for (int i = 0; i < num; i++) {
            result.add(transactionsList.get(i));
        }

        return result;
    }

    private String createId() {
        return String.valueOf(random.nextInt(500));
    }

    public void createNewBankAccountForUser(User user) {
        BankAccount bankAccount = new BankAccount(createId(), BigDecimal.ZERO, user, new ArrayList<>());
        if (bankAccount != null) {
            user.getBankAccounts().add(bankAccount);
            System.out.println("BankAccount was created");
            System.out.println("New bank account: " + user.getBankAccounts());
        } else {
            System.out.println("Incorrect data");
        }
    }

    public void deleteUserBankAccount(User user, BankAccount bankAccount) {
        if(user == null){
            System.out.println("User not found");
        }
        if (user.getBankAccounts().contains(bankAccount)) {
            user.getBankAccounts().remove(bankAccount);
            System.out.println("BankAccount " + bankAccount + " was deleted");
        } else {
            System.out.println("Account not found");
        }
    }

    public void replenishmentBankAccount(BankAccount bankAccount, BigDecimal sum) {
        BigDecimal balance = bankAccount.getBalance();

        if (sum.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(sum);
            bankAccount.getTransactions().add(new Transaction(createId(), sum, DEPOSIT, "Replenishment",
                    bankAccount, LocalDateTime.now()));
            System.out.println("Balance: " + balance);
        } else {
            System.out.println("Incorrect sum");
        }
    }

    public void paymentFromBankAccount(BankAccount bankAccount, Transaction transaction) {
        List<Transaction> transactions = bankAccount.getTransactions();

        if (transaction.getValue() == null) {
            System.out.println("Incorrect value");
        }

        if (bankAccount.getBalance().compareTo(transaction.getValue()) >= 0) {
            System.out.println("Account balance before: " + bankAccount.getBalance());
            bankAccount.setBalance(bankAccount.getBalance().subtract(transaction.getValue()));

            transactions.add(transaction);
            System.out.println("Account balance after: " + bankAccount.getBalance());
        } else {
            System.out.println("Insufficient funds in the account");
        }
    }

    public void paymentFromAndToAccount(BankAccount fromAccount, BankAccount toAccount, BigDecimal sum) {
        System.out.println("Start balance fromAccount: " + fromAccount.getBalance());
        System.out.println("Start balance toAccount: " + toAccount.getBalance());
        System.out.println();

        if (fromAccount.getBalance().compareTo(sum) >= 0) {
            fromAccount.setBalance(fromAccount.getBalance().subtract(sum));
            toAccount.setBalance(toAccount.getBalance().add(sum));

            Transaction transactionFrom = new Transaction(createId(), sum, TRANSFER, "Transfer",
                    fromAccount, LocalDateTime.now());

            Transaction transactionTo = new Transaction(createId(), sum, TRANSFER, "Transfer",
                    toAccount, LocalDateTime.now());

            fromAccount.getTransactions().add(transactionFrom);
            toAccount.getTransactions().add(transactionTo);

            System.out.println("After balance fromAccount: " + fromAccount.getBalance());
            System.out.println("After balance toAccount: " + toAccount.getBalance());
            System.out.println();

            System.out.println(fromAccount.getTransactions());
            System.out.println(toAccount.getTransactions());
        }
    }
}
