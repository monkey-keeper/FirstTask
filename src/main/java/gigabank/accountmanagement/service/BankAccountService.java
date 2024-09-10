package gigabank.accountmanagement.service;

import gigabank.accountmanagement.dao.BankAccountDAO;
import gigabank.accountmanagement.entity.BankAccount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис отвечает за управление счетами, включая создание, удаление и пополнение
 */
@Service
@AllArgsConstructor
public class BankAccountService {
    //  private Map<User, List<BankAccount>> userAccounts;

    private final BankAccountDAO accountDAO;


    public List<BankAccount> getBankAccounts() {
        return accountDAO.getBankAccounts();
    }

    public BankAccount getBankAccount(String id) {
        return accountDAO.getBankAccount(id);
    }

    public void createBankAccount(BankAccount bankAccount) {
        accountDAO.AddBankAccount(bankAccount);
    }

    public void updateBankAccount(String id, BankAccount bankAccount) {
        accountDAO.UpdateBankAccount(id, bankAccount);
    }

    public void deleteBankAccount(String id) {
        accountDAO.DeleteBankAccount(id);
    }

}
