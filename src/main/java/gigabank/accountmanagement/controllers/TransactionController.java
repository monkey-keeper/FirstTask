package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dao.TransactionDAO;
import gigabank.accountmanagement.dao.UserDAO;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionDAO transactionDAO;
    private final TransactionService transactionService;
    private final UserDAO userDAO;


    @GetMapping()
    public List<Transaction> getAllTransactions() {
        return transactionDAO.getTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable String id) {
        return transactionDAO.getTransactionById(id);
    }

    // как спринг будет различать id от category?
    @GetMapping("/{category}")
    public List<Transaction> getTransactionByCategory(@PathVariable String category) {
        Predicate<Transaction> predicate = transaction -> transaction.getCategory().equals(category);
        return userDAO.findAll().stream()
                .flatMap(user -> transactionService.filterTransactions(user, predicate).stream())
                .collect(Collectors.toList());
    }

    // как спринг будет различать id от category и от type?
    @GetMapping("/{type}")
    public List<Transaction> getTransactionByType(@PathVariable String type) {
        Predicate<Transaction> predicate = transaction -> transaction.getType().toString().equals(type);
        return userDAO.findAll().stream()
                .flatMap(user -> transactionService.filterTransactions(user, predicate).stream())
                .collect(Collectors.toList());
    }

    @PostMapping()
    public void createTransaction(@RequestBody Transaction transaction) {
        transactionDAO.createTransaction(transaction);
    }

    @PostMapping("/{id}")
    public void updateTransaction(@PathVariable String id, @RequestBody Transaction transaction) {
        transactionDAO.updateTransaction(id, transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable String id) {
        transactionDAO.deleteTransaction(id);
    }




}
