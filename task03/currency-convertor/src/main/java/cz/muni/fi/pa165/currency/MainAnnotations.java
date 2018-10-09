package cz.muni.fi.pa165.currency;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainAnnotations {



    public static void main(String... args) {

        springAnnotationContext();
    }

    private static void springAnnotationContext() {

        ApplicationContext applicationContext
                = new AnnotationConfigApplicationContext("cz.muni.fi.pa165");

        CurrencyConvertor Convertor = applicationContext.getBean(CurrencyConvertor.class);

        BigDecimal Result = Convertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), new BigDecimal("3"));
        System.out.println("The result is: " + Result.toString() + "CZK");
    }
}
