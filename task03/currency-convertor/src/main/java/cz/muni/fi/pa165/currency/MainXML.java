package cz.muni.fi.pa165.currency;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainXML {



    public static void main(String... args) {

        springXmlContext();
    }

    private static void springXmlContext() {

        ApplicationContext applicationContext
                = new ClassPathXmlApplicationContext("applicationContext.xml");

        CurrencyConvertor Convertor = applicationContext.getBean(CurrencyConvertor.class);

        BigDecimal Result = Convertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), new BigDecimal("2"));
        System.out.println("The result is: " + Result.toString() + "CZK");
    }
}

