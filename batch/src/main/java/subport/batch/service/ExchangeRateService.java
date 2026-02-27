package subport.batch.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.batch.external.ExchangeRateApi;
import subport.batch.persistence.SpringDataExchangeRateRepository;
import subport.domain.exchangeRate.ExchangeRate;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeRateService {

	private final SpringDataExchangeRateRepository exchangeRateRepository;
	private final ExchangeRateApi exchangeRateApi;

	public ExchangeRate getExchangeRate(LocalDate startDate, LocalDateTime now) {
		LocalDate startWeekdayDate = getLastWeekdayDate(startDate);

		ExchangeRate exchangeRate = exchangeRateRepository.findByRequestDate(startWeekdayDate)
			.orElse(null);
		if (exchangeRate != null) {
			LocalDateTime noon = startWeekdayDate.atTime(12, 0);

			if (exchangeRate.getApplyDate().equals(startWeekdayDate.minusDays(1))
				&& exchangeRate.getLastModifiedAt().isBefore(noon)
				&& !now.isBefore(noon)) {
				BigDecimal rate = exchangeRateApi.fetch(startWeekdayDate.toString());
				if (rate != null) {
					exchangeRate.updateRate(startWeekdayDate, rate);
				}
			}

			return exchangeRate;
		}

		LocalDate targetDate = startWeekdayDate;
		BigDecimal rate = null;
		while (rate == null) {
			rate = exchangeRateApi.fetch(targetDate.toString());
			if (rate == null) {
				targetDate = targetDate.minusDays(1);
			}
		}

		exchangeRate = new ExchangeRate(
			startWeekdayDate,
			targetDate,
			rate
		);
		exchangeRateRepository.save(exchangeRate);

		return exchangeRate;
	}

	private LocalDate getLastWeekdayDate(LocalDate date) {
		return switch (date.getDayOfWeek()) {
			case SATURDAY -> date.minusDays(1);
			case SUNDAY -> date.minusDays(2);
			default -> date;
		};
	}
}
