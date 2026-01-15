package subport.adapter.out.external;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExchangeRateResponse(
	@JsonProperty("cur_unit") String currencyUnit,
	@JsonProperty("deal_bas_r") String exchangeRate
) {

	public BigDecimal exchangeRateToDecimal() {
		return new BigDecimal(exchangeRate.replace(",", ""));
	}
}
