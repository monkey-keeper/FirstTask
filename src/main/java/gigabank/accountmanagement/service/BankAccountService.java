package gigabank.accountmanagement.service;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.repository.BankAccountRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    private EntityManager entityManager;


    public List<BankAccount> getBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public BankAccount getBankAccount(Long id) {
        return bankAccountRepository.findById(id).orElse(null);
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public void createBankAccountForUser(User user) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setOwner(entityManager.getReference(User.class, user.getId()));
        bankAccount.setBalance(new BigDecimal("0.0"));
        bankAccountRepository.save(bankAccount);
    }

    public BankAccount updateBankAccount(Long id, BankAccount bankAccount) {
        bankAccount.setId(id);
        return bankAccountRepository.save(bankAccount);
    }

    public void deleteBankAccount(Long id) {
        bankAccountRepository.deleteById(id);
    }

}
