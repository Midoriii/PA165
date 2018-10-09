package cz.muni.fi.pa165.currency;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;


public class MainJavaConfig {

    public static void main(String... args) {

        springJavaConfigContext();
    }

    private static void springJavaConfigContext() {

        ApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(JavaConfig.class);

        CurrencyConvertor Convertor = applicationContext.getBean(CurrencyConvertor.class);

        BigDecimal Result = Convertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), new BigDecimal("4"));
        System.out.println("The result is: " + Result.toString() + "CZK");

    }
}
