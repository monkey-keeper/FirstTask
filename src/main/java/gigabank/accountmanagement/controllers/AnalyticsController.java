package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.dto.TransactionDTO;
import gigabank.accountmanagement.mapper.TransactionMapper;
import gigabank.accountmanagement.service.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/analytics")
@AllArgsConstructor
public class AnalyticsController {
    public final AnalyticsService analyticsService;

    @GetMapping("/largest-transactions-from-bank-account/{accountId}")
    public TransactionDTO getLargestTransactionFromBankAccount(@PathVariable Long accountId) {
        return TransactionMapper.toDTO(analyticsService.getLargestTransactionsFromBankAccount(accountId));
    }

    @GetMapping("/smallest-transaction-from-bank-account/{accountId}")
    public TransactionDTO getSmallestTransactionFromBankAccount(@PathVariable Long accountId) {
        return TransactionMapper.toDTO(analyticsService.getSmallestTransactionsFromBankAccount(accountId));
    }

    @GetMapping("/average-transaction-from-bank-account/{accountId}")
    public TransactionDTO getAverageTransactionFromBankAccount(@PathVariable Long accountId) {
        return TransactionMapper.toDTO(analyticsService.getAverageTransactionsFromBankAccount(accountId));
    }

    @GetMapping("/sum-transaction-by-category-from-bank-account/{accountId}")
    public BigDecimal getSumTransactionByCategoryFromBankAccount(@PathVariable Long accountId,
                                                                 @RequestParam String category) {
        return analyticsService.getSumOfTransactionsByCategoryFromBankAccount(category, accountId);
    }

    @GetMapping("/sum-transaction-by-date-range-from-bank-account/{accountId}")
    public BigDecimal getSumTransactionByDateRangeFromBankAccount(@PathVariable Long accountId,
                                                                  @RequestParam LocalDateTime startDate,
                                                                  @RequestParam LocalDateTime endDate) {
        return analyticsService.getSumOfTransactionsByDateRangeFromBankAccount(startDate, endDate, accountId);
    }

}
