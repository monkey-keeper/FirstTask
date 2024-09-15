package gigabank.accountmanagement.mapper;

import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.entity.BankAccount;

public class BankAccountMapper {
    public static BankAccountDTO toDTO(BankAccount bankAccount) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setId(bankAccount.getId());
        bankAccountDTO.setBalance(bankAccount.getBalance());
        bankAccountDTO.setOwner(UserMapper.toDTO(bankAccount.getOwner()));
        return bankAccountDTO;
    }

    public static BankAccount fromDTO(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(bankAccountDTO.getId());
        bankAccount.setBalance(bankAccountDTO.getBalance());
        bankAccount.setOwner(UserMapper.fromDTO(bankAccountDTO.getOwner()));
        return bankAccount;
    }

}
