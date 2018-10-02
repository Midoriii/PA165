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

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertorImplTest {
	
	private static Currency CZK = Currency.getInstance("CZK");
	private static Currency EUR = Currency.getInstance("EUR");
	 
	private CurrencyConvertor currencyConvertor;
	private BigDecimal rate;
	 
	//Mock table
	@Mock
	private ExchangeRateTable exchangeRateTable;

	//Inicializace pred
	@Before
	public void init() {	
	    MockitoAnnotations.initMocks(this);
	    currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);
	    rate = new BigDecimal("0.1");
   }

	@Rule
	public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void testConvert() throws ExternalServiceFailureException{
        //pri volani metody mocknout result
    	when(exchangeRateTable.getExchangeRate(EUR, CZK))
        	.thenReturn(rate);

		assertEquals(new BigDecimal("1.00"), currencyConvertor.convert(EUR, CZK, new BigDecimal("10.050")));  //String kvuli zaokrouhlovani
		assertEquals(new BigDecimal("1.01"), currencyConvertor.convert(EUR, CZK, new BigDecimal("10.051")));
		assertEquals(new BigDecimal("1.01"), currencyConvertor.convert(EUR, CZK, new BigDecimal("10.149")));
		assertEquals(new BigDecimal("1.02"), currencyConvertor.convert(EUR, CZK, new BigDecimal("10.150")));
        // Don't forget to test border values and proper rounding.
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
        	.thenThrow(UnknownExchangeRateException.class);
    	expectedException.expect(UnknownExchangeRateException.class);
    	currencyConvertor.convert(EUR, CZK, BigDecimal.ONE);
    }

}
