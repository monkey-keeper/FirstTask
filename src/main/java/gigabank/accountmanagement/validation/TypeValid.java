package gigabank.accountmanagement.validation;

import gigabank.accountmanagement.entity.TransactionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class TypeValid implements ConstraintValidator<TypeContain, TransactionType> {
    @Override
    public void initialize(TypeContain constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TransactionType transactionType, ConstraintValidatorContext constraintValidatorContext) {
        return transactionType != null && Arrays.asList(TransactionType.values()).contains(transactionType);
    }


}
