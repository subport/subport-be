package subport.domain.exchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ExchangeRate {

	private final LocalDate requestDate;
	private LocalDate applyDate;
	private BigDecimal rate;
	private final LocalDateTime lastModifiedAt;

	public ExchangeRate(
		LocalDate requestDate,
		LocalDate applyDate,
		BigDecimal rate,
		LocalDateTime lastModifiedAt
	) {
		this.requestDate = requestDate;
		this.applyDate = applyDate;
		this.rate = rate;
		this.lastModifiedAt = lastModifiedAt;
	}

	public void updateRate(LocalDate applyDate, BigDecimal rate) {
		this.applyDate = applyDate;
		this.rate = rate;
	}
}
