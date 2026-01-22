package subport.application.exchangeRate.port.out;

import java.math.BigDecimal;

public interface FetchExchangeRatePort {

	BigDecimal fetch(String searchDate);
}
