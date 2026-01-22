package subport.application.exchangeRate.port.out;

import java.time.LocalDate;

import subport.domain.exchangeRate.ExchangeRate;

public interface LoadExchangeRatePort {

	ExchangeRate load(LocalDate requestDate);
}
