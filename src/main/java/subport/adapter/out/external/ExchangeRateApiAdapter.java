package subport.adapter.out.external;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import subport.application.exchangeRate.port.out.FetchExchangeRatePort;
import subport.domain.subscription.AmountUnit;

@Component
@RequiredArgsConstructor
public class ExchangeRateApiAdapter implements FetchExchangeRatePort {

	private final RestClient exchangeRateClient;

	@Value("${koreaexim.authkey}")
	private String authKey;

	@Value("${koreaexim.data}")
	private String data;

	@Override
	public BigDecimal fetch(String searchDate) {
		List<ExchangeRateResponse> responses = exchangeRateClient.get()
			.uri(uriBuilder -> uriBuilder
				.queryParam("authkey", authKey)
				.queryParam("data", data)
				.queryParam("searchdate", searchDate)
				.build())
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});

		return responses.stream()
			.filter(r -> r.currencyUnit().equals(AmountUnit.USD.name()))
			.findFirst()
			.map(ExchangeRateResponse::exchangeRateToDecimal)
			.orElse(null);
	}
}
