package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.mapper.BankAccountMapper;
import gigabank.accountmanagement.service.BankAccountService;
import jakarta.validation.Valid;
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
                .map(BankAccountMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public BankAccountDTO getBankAccountById(@PathVariable("id") String id) {
        return BankAccountMapper.toDTO(bankAccountService.getBankAccount(id));
    }

    @PostMapping()
    public BankAccountDTO createBankAccount(@RequestBody @Valid BankAccountDTO bankAccountDTO) {
        return BankAccountMapper.toDTO(bankAccountService.createBankAccount(BankAccountMapper.fromDTO(bankAccountDTO)));
    }

    @PostMapping("/{id}")
    public BankAccountDTO updateBankAccount(@PathVariable("id") Long id,
                                            @RequestBody @Valid BankAccountDTO bankAccountDTO) {
        return BankAccountMapper.toDTO(bankAccountService.updateBankAccount(id,
                BankAccountMapper.fromDTO(bankAccountDTO)));
    }

    @DeleteMapping("/{id}")
    public void deleteBankAccount(@PathVariable("id") String id) {
        bankAccountService.deleteBankAccount(id);
    }

}
