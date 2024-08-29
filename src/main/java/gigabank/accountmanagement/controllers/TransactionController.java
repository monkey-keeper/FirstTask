package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.TransactionDTO;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.mapper.TransactionMapper;
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
    private final TransactionService transactionService;


    @GetMapping()
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable String id) {
        return transactionService.findById(id);
    }

    // как спринг будет различать id от category?
    @GetMapping("/{category}")
    public List<TransactionDTO> getTransactionByCategory(@PathVariable String category) {
        return transactionService.getTransactionByCategory(category);
    }

    // как спринг будет различать id от category и от type?
    @GetMapping("/{type}")
    public List<TransactionDTO> getTransactionByType(@PathVariable String type) {
        return transactionService.getTransactionByType(type);
    }

    @PostMapping()
    public void createTransaction(@RequestBody Transaction transaction) {
        transactionService.create(TransactionMapper.convertToDTO(transaction));
    }

    @PostMapping("/{id}")
    public void updateTransaction(@PathVariable String id, @RequestBody Transaction transaction) {
        transactionService.update(id, TransactionMapper.convertToDTO(transaction));
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable String id) {
        transactionService.delete(id);
    }




}
