package cz.muni.fi.pa165.currency;

import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@ComponentScan("cz.muni.fi.pa165.currency")
@EnableAspectJAutoProxy
public class JavaConfig {

    @Inject
    private ExchangeRateTable ExchangeTable;

    @Bean
    public CurrencyConvertor CurrConvertor() {
        System.err.println("Creating Currency Convertor");
        return new CurrencyConvertorImpl(ExchangeTable);
    }

}