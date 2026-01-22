package subport.adapter.out.persistence.exchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;

@Entity
@Table(name = "exchange_rate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExchangeRateJpaEntity extends BaseTimeEntity {

	@Id
	@Column(updatable = false)
	private LocalDate requestDate;

	private LocalDate applyDate;

	private BigDecimal rate;

	public ExchangeRateJpaEntity(
		LocalDate requestDate,
		LocalDate applyDate,
		BigDecimal rate
	) {
		this.requestDate = requestDate;
		this.applyDate = applyDate;
		this.rate = rate;
	}
}
