package subport.adapter.out.persistence.exchangeRate;

import org.springframework.stereotype.Component;

import subport.domain.exchangeRate.ExchangeRate;

@Component
public class ExchangeRateMapper {

	public ExchangeRateJpaEntity toJpaEntity(ExchangeRate exchangeRate) {
		return new ExchangeRateJpaEntity(
			exchangeRate.getRequestDate(),
			exchangeRate.getApplyDate(),
			exchangeRate.getRate()
		);
	}

	public ExchangeRate toDomain(ExchangeRateJpaEntity exchangeRateEntity) {
		return new ExchangeRate(
			exchangeRateEntity.getRequestDate(),
			exchangeRateEntity.getApplyDate(),
			exchangeRateEntity.getRate(),
			exchangeRateEntity.getLastModifiedAt()
		);
	}
}
