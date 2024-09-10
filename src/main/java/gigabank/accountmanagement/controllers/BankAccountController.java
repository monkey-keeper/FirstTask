package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.mapper.BankAccountMapper;
import gigabank.accountmanagement.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bank-account")
@AllArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping()
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountService.getBankAccounts().stream()
                .map(BankAccountMapper::convertToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public BankAccountDTO getBankAccountById(@PathVariable String id) {
        return BankAccountMapper.convertToDTO(bankAccountService.getBankAccount(id));
    }

    @PostMapping()
    public void createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        bankAccountService.createBankAccount(BankAccountMapper.convertToEntity(bankAccountDTO));
    }

    @PostMapping("/{id}")
    public void updateBankAccount(@PathVariable String id, @RequestBody BankAccountDTO bankAccountDTO) {
        bankAccountService.updateBankAccount(id, BankAccountMapper.convertToEntity(bankAccountDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteBankAccount(@PathVariable String id) {
        bankAccountService.deleteBankAccount(id);
    }


}
