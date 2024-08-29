package gigabank.accountmanagement.dto;

import gigabank.accountmanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    private String id;
    private BigDecimal balance;
    private User owner;
}
