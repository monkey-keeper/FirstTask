package gigabank.accountmanagement.repository;

import gigabank.accountmanagement.entity.BankAccount;

import java.math.BigInteger;
import java.util.List;

public interface BankAccountRepository {
    BankAccount create(BankAccount bankAccount);
    BankAccount findById(BigInteger id);
    List<BankAccount> findAll();
    BankAccount update(BankAccount bankAccount);
    void delete(BigInteger id);
}
