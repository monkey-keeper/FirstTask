package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.TransactionDTO;
import gigabank.accountmanagement.mapper.TransactionMapper;
import gigabank.accountmanagement.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;


    @GetMapping()
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.findAll().stream()
                .map(TransactionMapper::convertToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable String id) {
        return TransactionMapper.convertToDTO(transactionService.findById(id));
    }

    @GetMapping("/search")
    public List<TransactionDTO> searchTransactions(@RequestParam(required = false) String category,
                                                   @RequestParam(required = false) String type) {
        if (category == null && type == null) {
            return getAllTransactions();
        } else if (category != null && type != null) {
            return transactionService.getTransactionByCategoryAndType(category, type).stream()
                    .map(TransactionMapper::convertToDTO)
                    .toList();
        } else if (category != null) {
            return transactionService.getTransactionByCategory(category).stream()
                    .map(TransactionMapper::convertToDTO)
                    .toList();
        } else {
            return transactionService.getTransactionByType(type).stream()
                    .map(TransactionMapper::convertToDTO)
                    .toList();
        }
    }

    @PostMapping()
    public void createTransaction(@RequestBody TransactionDTO transactionDTO) {
        transactionService.create(TransactionMapper.convertToEntity(transactionDTO));
    }

    @PostMapping("/{id}")
    public void updateTransaction(@PathVariable String id, @RequestBody TransactionDTO transactionDTO) {
        transactionService.update(id, TransactionMapper.convertToEntity(transactionDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable String id) {
        transactionService.delete(id);
    }


}
