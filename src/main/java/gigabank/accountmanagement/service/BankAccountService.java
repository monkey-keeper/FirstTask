package gigabank.accountmanagement.service;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * Сервис отвечает за управление счетами, включая создание, удаление и пополнение
 */
@Service
@AllArgsConstructor
public class BankAccountService {
    //  private Map<User, List<BankAccount>> userAccounts;

    @Autowired
    private BankAccountRepository bankAccountRepository;


    public List<BankAccount> getBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public BankAccount getBankAccount(String id) {
        return bankAccountRepository.findById(BigInteger.valueOf(Long.parseLong(id)));
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.create(bankAccount);
    }

    public BankAccount updateBankAccount(String id, BankAccount bankAccount) {
        bankAccount.setId(id);
        return bankAccountRepository.update(bankAccount);
    }

    public void deleteBankAccount(String id) {
        bankAccountRepository.delete(BigInteger.valueOf(Long.parseLong(id)));
    }

}
