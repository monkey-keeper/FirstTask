package gigabank.accountmanagement.service;

import gigabank.accountmanagement.dao.BankAccountDAO;
import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.mapper.BankAccountMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис отвечает за управление счетами, включая создание, удаление и пополнение
 */
@Service
@AllArgsConstructor
public class BankAccountService {
    //  private Map<User, List<BankAccount>> userAccounts;

    private final BankAccountDAO accountDAO;


    public List<BankAccountDTO> getBankAccounts() {
        return accountDAO.getBankAccounts().stream()
                .map(BankAccountMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public BankAccountDTO getBankAccount(String id) {
        return BankAccountMapper.convertToDTO(accountDAO.getBankAccount(id));
    }

    public void createBankAccount(BankAccountDTO bankAccountDTO) {
        accountDAO.AddBankAccount(BankAccountMapper.convertToEntity(bankAccountDTO));
    }

    public void updateBankAccount(String id, BankAccountDTO bankAccountDTO) {
        accountDAO.UpdateBankAccount(id, BankAccountMapper.convertToEntity(bankAccountDTO));
    }

    public void deleteBankAccount(String id) {
        accountDAO.DeleteBankAccount(id);
    }

}
