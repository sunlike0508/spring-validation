package hello.itemservice.validation;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

@SpringBootTest
public class MessageCodesResolverTest {

    MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();

    @Test
    void setMessageCodesResolver() {
        String[] messageCode = messageCodesResolver.resolveMessageCodes("required", "item");

        Arrays.stream(messageCode).forEach(System.out::println);
    }


    @Test
    void setMessageCodesResolver2() {
        String[] messageCode = messageCodesResolver.resolveMessageCodes("required", "item", "itemNames", String.class);

        Arrays.stream(messageCode).forEach(System.out::println);
    }

}
