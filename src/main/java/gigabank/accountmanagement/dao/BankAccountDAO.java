package gigabank.accountmanagement.dao;

import gigabank.accountmanagement.entity.BankAccount;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BankAccountDAO {

    public List<BankAccount> getBankAccounts() {
        // тут должна быть реализация SQL
        return null;
    }

    public BankAccount getBankAccount(String id) {
        // тут должна быть реализация SQL
        return null;
    }

    public void UpdateBankAccount(String id, BankAccount bankAccount) {
        // тут должна быть реализация SQL
    }

    public void DeleteBankAccount(String id) {
        // тут должна быть реализация SQL

    }

    public void AddBankAccount(BankAccount bankAccount) {
        // тут должна быть реализация SQL

    }

}
