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



}
