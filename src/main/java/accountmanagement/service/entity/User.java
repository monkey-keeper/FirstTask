package accountmanagement.service.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Информация о пользователе
 */
public class User {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private List<BankAccount> bankAccounts;
}
