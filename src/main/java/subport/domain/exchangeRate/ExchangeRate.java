package subport.domain.exchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

@Getter
public class ExchangeRate {

	private final LocalDate requestDate;
	private final LocalDate applyDate;
	private final BigDecimal rate;

	public ExchangeRate(
		LocalDate requestDate,
		LocalDate applyDate,
		BigDecimal rate
	) {
		this.requestDate = requestDate;
		this.applyDate = applyDate;
		this.rate = rate;
	}
}
