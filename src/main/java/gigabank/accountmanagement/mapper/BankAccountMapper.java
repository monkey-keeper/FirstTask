package gigabank.accountmanagement.mapper;

import gigabank.accountmanagement.dao.BankAccountDAO;
import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.entity.BankAccount;

public class BankAccountMapper {
    public static BankAccountDTO convertToDTO(BankAccount bankAccount) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setId(bankAccount.getId());
        bankAccountDTO.setBalance(bankAccount.getBalance());
        bankAccountDTO.setOwner(bankAccount.getOwner());
        return bankAccountDTO;
    }

    public static BankAccount convertToEntity(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(bankAccountDTO.getId());
        bankAccount.setBalance(bankAccountDTO.getBalance());
        bankAccount.setOwner(bankAccountDTO.getOwner());
        return bankAccount;
    }

}
