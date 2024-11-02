package gigabank.accountmanagement.service;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class KafkaService {
    private final BankAccountRepository bankAccountRepository;

    @KafkaListener(topics = "${tpd.topic-name}")
    public void listen(User user) {
        createBankAccountForUser(user);
    }

    private void createBankAccountForUser(User user) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setOwner(user);
        bankAccount.setBalance(new BigDecimal("0.0"));
        bankAccountRepository.save(bankAccount);
    }

}
