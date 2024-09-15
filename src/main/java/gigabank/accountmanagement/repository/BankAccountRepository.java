package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;

import java.util.List;

public interface BankAccountRepository {
    BankAccount create(BankAccount bankAccount);
    BankAccount findById(String id);
    List<BankAccount> findAll();
    BankAccount update(BankAccount bankAccount);
    void delete(String id);
}
