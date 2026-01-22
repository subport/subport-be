package subport.application.exchangeRate.port.out;

import subport.domain.exchangeRate.ExchangeRate;

public interface SaveExchangeRatePort {

	void save(ExchangeRate exchangeRate);
}
