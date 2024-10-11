package gigabank.accountmanagement.controllers;

import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.mapper.BankAccountMapper;
import gigabank.accountmanagement.service.BankAccountService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public BankAccountDTO createBankAccount(@RequestBody @Valid BankAccountDTO bankAccountDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorMessage.toString());
        }
        return BankAccountMapper.toDTO(bankAccountService.createBankAccount(BankAccountMapper.fromDTO(bankAccountDTO)));
    }

    @PostMapping("/{id}")
    public BankAccountDTO updateBankAccount(@PathVariable("id") Long id, @RequestBody @Valid BankAccountDTO bankAccountDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorMessage.toString());
        }
        return BankAccountMapper.toDTO(bankAccountService.updateBankAccount(id, BankAccountMapper.fromDTO(bankAccountDTO)));
    }

    @DeleteMapping("/{id}")
    public void deleteBankAccount(@PathVariable("id") String id) {
        bankAccountService.deleteBankAccount(id);
    }


}
