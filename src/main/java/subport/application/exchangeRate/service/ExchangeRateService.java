package subport.application.exchangeRate.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exchangeRate.port.out.FetchExchangeRatePort;
import subport.application.exchangeRate.port.out.LoadExchangeRatePort;
import subport.application.exchangeRate.port.out.SaveExchangeRatePort;
import subport.domain.exchangeRate.ExchangeRate;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeRateService {

	private final LoadExchangeRatePort loadExchangeRatePort;
	private final FetchExchangeRatePort fetchExchangeRatePort;
	private final SaveExchangeRatePort saveExchangeRatePort;

	public ExchangeRate getExchangeRate(LocalDate startDate, LocalDateTime currentDateTime) {
		LocalDate startWeekdayDate = getLastWeekdayDate(startDate);

		ExchangeRate exchangeRate = loadExchangeRatePort.load(startWeekdayDate);
		if (exchangeRate != null) {
			LocalDateTime noon = startWeekdayDate.atTime(12, 0);

			if (exchangeRate.getApplyDate().equals(startWeekdayDate.minusDays(1))
				&& exchangeRate.getLastModifiedAt().isBefore(noon)
				&& !currentDateTime.isBefore(noon)) {
				BigDecimal rate = fetchExchangeRatePort.fetch(startWeekdayDate.toString());
				if (rate != null) {
					exchangeRate.updateRate(startWeekdayDate, rate);
				}
			}

			return exchangeRate;
		}

		LocalDate targetDate = startWeekdayDate;
		BigDecimal rate = null;
		while (rate == null) {
			rate = fetchExchangeRatePort.fetch(targetDate.toString());
			if (rate == null) {
				targetDate = targetDate.minusDays(1);
			}
		}

		exchangeRate = new ExchangeRate(
			startWeekdayDate,
			targetDate,
			rate
		);
		saveExchangeRatePort.save(exchangeRate);

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
