package subport.application.exchangeRate.port.out;

import subport.domain.exchangeRate.ExchangeRate;

public interface UpdateExchangeRatePort {

	void update(ExchangeRate exchangeRate);
}
