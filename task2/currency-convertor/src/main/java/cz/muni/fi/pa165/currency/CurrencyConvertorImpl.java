package cz.muni.fi.pa165.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private ExchangeRateTable exchangeRateTable;

    //Create logger on what class
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    //Inicializace
    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {

        //What to trace - what are we converting
        logger.trace("convert({},{},{})",sourceCurrency, targetCurrency, sourceAmount);

        //Overeni postupne tradicnich chyb, viz interface
        if (sourceCurrency == null) {
            throw new IllegalArgumentException("sourceCurrency is null");
        }
        if (targetCurrency == null) {
            throw new IllegalArgumentException("targetCurrency is null");
        }
        if (sourceAmount == null) {
            throw new IllegalArgumentException("sourceAmount is null");
        }
        try {
            //Vemem exchange rate
            BigDecimal exchangeRate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);

            //Kdyz nenajdem exchangeRate, je null a hodime exception
            if (exchangeRate == null) {
                //Warning od loggeru .. whatever
                logger.warn("Exchange rate is {}", exchangeRate);

                throw new UnknownExchangeRateException("ExchangeRate is unknown");
            }

            return exchangeRate.multiply(sourceAmount).setScale(2, RoundingMode.HALF_EVEN);  //zaokrouhli na sudeho souseda

            //If whatever fails, catch this, thrown by exchange rate
        } catch (ExternalServiceFailureException ex) {
            //Lognuti erroru .. ?
            logger.error("External service error");
            //logger.error(ex.getMessage(), ex);

            throw new UnknownExchangeRateException("Error when fetching exchange rate", ex);
        }
    }

}
