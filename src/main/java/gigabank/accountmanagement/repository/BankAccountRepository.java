package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;

import java.util.List;

public interface BankAccountRepository {
    BankAccount save(BankAccount bankAccount);
    BankAccount findById(String id);
    List<BankAccount> findAll();
    BankAccount update(String id, BankAccount bankAccount);
    void delete(BankAccount bankAccount);
}
