package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dao.BankAccountDAO;
import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-account")
@AllArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping()
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountService.getBankAccounts();
    }

    @GetMapping("/{id}")
    public BankAccountDTO getBankAccountById(@PathVariable String id) {
        return bankAccountService.getBankAccount(id);
    }

    @PostMapping()
    public void createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        bankAccountService.createBankAccount(bankAccountDTO);
    }

    @PostMapping("/{id}")
    public void updateBankAccount(@PathVariable String id, @RequestBody BankAccountDTO bankAccountDTO) {
        bankAccountService.updateBankAccount(id, bankAccountDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteBankAccount(@PathVariable String id) {
        bankAccountService.deleteBankAccount(id);
    }

}
