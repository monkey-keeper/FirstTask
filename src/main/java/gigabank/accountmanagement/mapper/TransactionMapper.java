package gigabank.accountmanagement.mapper;

import gigabank.accountmanagement.dto.TransactionDTO;
import gigabank.accountmanagement.entity.Transaction;

public class TransactionMapper {
    public static TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getId());
        transactionDTO.setValue(transaction.getValue());
        transactionDTO.setType(transaction.getType());
        transactionDTO.setCategory(transaction.getCategory());
        return transactionDTO;
    }

    public static Transaction fromDTO(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getTransactionId());
        transaction.setValue(transactionDTO.getValue());
        transaction.setType(transactionDTO.getType());
        transaction.setCategory(transactionDTO.getCategory());
        return transaction;
    }
}
