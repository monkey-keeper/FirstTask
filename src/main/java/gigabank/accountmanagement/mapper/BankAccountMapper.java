package gigabank.accountmanagement.mapper;

import gigabank.accountmanagement.dto.BankAccountDTO;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

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

    public static BankAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Извлекаем дату рождения как LocalDate
        LocalDate birthDate = rs.getTimestamp("birthdate").toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Создаём пользователя с датой рождения
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setFirstName(rs.getString("firstName"));
        user.setMiddleName(rs.getString("middleName"));
        user.setLastName(rs.getString("lastName"));
        user.setBirthDate(birthDate);  // Передаём LocalDate
        user.setPhoneNumber(rs.getString("phoneNumber"));

        // Создаём банковский счёт
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(rs.getLong("bank_account_id"));
        bankAccount.setBalance(rs.getBigDecimal("balance"));
        bankAccount.setOwner(user);  // Связываем пользователя с банковским счётом

        return bankAccount;
    }

}
