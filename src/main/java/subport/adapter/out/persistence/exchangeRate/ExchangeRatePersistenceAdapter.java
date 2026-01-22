package subport.adapter.out.persistence.exchangeRate;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.exchangeRate.port.out.LoadExchangeRatePort;
import subport.application.exchangeRate.port.out.SaveExchangeRatePort;
import subport.domain.exchangeRate.ExchangeRate;

@Component
@RequiredArgsConstructor
public class ExchangeRatePersistenceAdapter implements
	SaveExchangeRatePort,
	LoadExchangeRatePort {

	private final SpringDataExchangeRateRepository exchangeRateRepository;
	private final ExchangeRateMapper exchangeRateMapper;

	@Override
	public void save(ExchangeRate exchangeRate) {
		ExchangeRateJpaEntity exchangeRateEntity = exchangeRateMapper.toJpaEntity(exchangeRate);

		exchangeRateRepository.save(exchangeRateEntity);
	}

	@Override
	public ExchangeRate load(LocalDate requestDate) {
		return exchangeRateRepository.findByRequestDate(requestDate)
			.map(exchangeRateMapper::toDomain)
			.orElse(null);
	}
}
