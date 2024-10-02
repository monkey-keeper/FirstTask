package gigabank.accountmanagement.mapper;

import gigabank.accountmanagement.dto.TransactionDTO;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class TransactionMapper {
    public static TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getId());
        transactionDTO.setValue(transaction.getValue());
        transactionDTO.setType(transaction.getType());
        transactionDTO.setCategory(transaction.getCategory());
        transactionDTO.setBankAccount(BankAccountMapper.toDTO(transaction.getBankAccount()));
        return transactionDTO;
    }

    public static Transaction fromDTO(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getTransactionId());
        transaction.setValue(transactionDTO.getValue());
        transaction.setType(transactionDTO.getType());
        transaction.setCategory(transactionDTO.getCategory());
        transaction.setBankAccount(BankAccountMapper.fromDTO(transactionDTO.getBankAccount()));
        return transaction;
    }

    public static Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDate birthDate = rs.getTimestamp("birthdate").toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Создаём объект UserAccount
        User user = new User();
        user.setId(rs.getString("user_id"));
        user.setFirstName(rs.getString("firstname"));
        user.setMiddleName(rs.getString("middlename"));
        user.setLastName(rs.getString("lastname"));
        user.setBirthDate(birthDate);

        // Создаём объект BankAccount
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(rs.getString("bankaccount_id"));
        bankAccount.setBalance(rs.getBigDecimal("balance"));
        bankAccount.setOwner(user);

        // Создаём объект Transaction
        Transaction transaction = new Transaction();
        transaction.setId(rs.getString("transaction_id"));
        transaction.setValue(rs.getBigDecimal("value"));
        transaction.setType(TransactionType.valueOf(rs.getString("type")));
        transaction.setCategory(rs.getString("category"));
        transaction.setCreatedDate(rs.getTimestamp("createDate").toLocalDateTime());
        transaction.setBankAccount(bankAccount);

        return transaction;
    }

}
