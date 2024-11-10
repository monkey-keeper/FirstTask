package gigabank.accountmanagement.listeners;

import gigabank.accountmanagement.dto.UserDTO;
import gigabank.accountmanagement.mapper.UserMapper;
import gigabank.accountmanagement.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankAccountListener {
    private final BankAccountService bankAccountService;

    @KafkaListener(topics = "${tpd.topic-name}", groupId = "bankAccount")
    public void listen(UserDTO userDTO) {
        bankAccountService.createBankAccountForUser(UserMapper.fromDTO(userDTO));
    }

}
