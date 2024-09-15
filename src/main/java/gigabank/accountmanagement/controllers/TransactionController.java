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
        return transactionService.findTransaction().stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable String id) {
        return TransactionMapper.toDTO(transactionService.findById(id));
    }

    @GetMapping("/search")
    public List<TransactionDTO> searchTransactions(@RequestParam(required = false) String category,
                                                   @RequestParam(required = false) String type) {
        return transactionService.findTransaction(category, type).stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    @PostMapping()
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return TransactionMapper.toDTO(transactionService.create(TransactionMapper.fromDTO(transactionDTO)));
    }

    @PostMapping("/{id}")
    public TransactionDTO updateTransaction(@PathVariable String id, @RequestBody TransactionDTO transactionDTO) {
        return TransactionMapper.toDTO(transactionService.update(id, TransactionMapper.fromDTO(transactionDTO)));
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable String id) {
        transactionService.delete(id);
    }


}
