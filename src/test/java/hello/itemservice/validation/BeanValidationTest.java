package hello.itemservice.validation;


import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


public class BeanValidationTest {

    static {
        Locale.setDefault(Locale.KOREA);
    }

    @Test
    void beanValidation() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

        Validator validator = validatorFactory.getValidator();

        Item item = new Item();
        item.setItemName(" ");
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> constraintViolations = validator.validate(item);

        for(ConstraintViolation<Item> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation);
            System.out.println(constraintViolation.getMessage());
        }

    }
}
