package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dao.BankAccountDAO;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-account")
public class BankAccountController {

    private final BankAccountDAO bankAccountDAO;

    BankAccountController(BankAccountDAO bankAccountDAO) {
        this.bankAccountDAO = bankAccountDAO;
    }

    @GetMapping()
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountDAO.getBankAccounts();
    }

    @GetMapping("/{id}")
    public BankAccount getBankAccountById(@PathVariable String id) {
        return bankAccountDAO.getBankAccount(id);
    }

    @PostMapping()
    public void createBankAccount(@RequestBody BankAccount bankAccount) {
        bankAccountDAO.AddBankAccount(bankAccount);
    }

    @PostMapping("/{id}")
    public void updateBankAccount(@PathVariable String id, @RequestBody BankAccount bankAccount) {
        bankAccountDAO.UpdateBankAccount(id, bankAccount);
    }

    @DeleteMapping("/{id}")
    public void deleteBankAccount(@PathVariable String id) {
        bankAccountDAO.DeleteBankAccount(id);
    }

}
