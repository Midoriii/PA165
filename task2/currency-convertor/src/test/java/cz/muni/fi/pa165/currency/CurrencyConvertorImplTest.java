package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

public class CurrencyConvertorImplTest {

    //Potrebujem meny
    private static Currency CZK = Currency.getInstance("CZK");
    private static Currency EUR = Currency.getInstance("EUR");

    private CurrencyConvertor currencyConvertor;

    //Rate of exchange
    private BigDecimal rate;

    //Mock table
    @Mock
    private ExchangeRateTable exchangeRateTable;

    //Inicializace pred
    @Before
    public void init() {
        //Inicialitace mocku
        MockitoAnnotations.initMocks(this);

        //Novej currencyConvertor i s mockem exhangeRateTable
        currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);

        //Zvoleni rate
        rate = new BigDecimal("0.1");
    }

    //Potrebujem ocekavanou exception co budem srovnavat
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testConvert() throws ExternalServiceFailureException{
        //pri volani metody mocknout result - vrati rate, predem pripravenou
        when(exchangeRateTable.getExchangeRate(EUR, CZK))
                .thenReturn(rate);

        //Testovani okrajovych hodnot pri zaokrouhlovani
        assertEquals(new BigDecimal("2.00"), currencyConvertor.convert(EUR, CZK, new BigDecimal("20.050")));  //String kvuli zaokrouhlovani
        assertEquals(new BigDecimal("5.01"), currencyConvertor.convert(EUR, CZK, new BigDecimal("50.051")));
        assertEquals(new BigDecimal("15.01"), currencyConvertor.convert(EUR, CZK, new BigDecimal("150.149")));
        assertEquals(new BigDecimal("60.02"), currencyConvertor.convert(EUR, CZK, new BigDecimal("600.150")));
    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        //Fill expectedException with what to expect
        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(null, CZK, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(EUR, null, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        expectedException.expect(IllegalArgumentException.class);
        currencyConvertor.convert(EUR, CZK, null);
    }

    //External service musime chytat pac to table vyhazuje
    @Test
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(EUR, CZK))
                .thenReturn(null);  //Returns null - doesn't know the exchange rate, getExchangeRate will be used in currencyConvertor.convert function

        expectedException.expect(UnknownExchangeRateException.class);
        currencyConvertor.convert(EUR, CZK, BigDecimal.ONE);
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(EUR, CZK))
                .thenThrow(ExternalServiceFailureException.class); //Hazem external service failure

        expectedException.expect(UnknownExchangeRateException.class);  //Ocekavame hozenou unknown exchange rate s popisem proc
        currencyConvertor.convert(EUR, CZK, BigDecimal.ONE);
    }

}
