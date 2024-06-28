package gigabank.accountmanagement.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * Информация о пользователе
 */
@Data
public class User {
    @NonNull
    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String middleName;
    @NonNull
    private String lastName;
    @NonNull
    private LocalDate birthDate;
    @NonNull
    private List<BankAccount> bankAccounts;
}
