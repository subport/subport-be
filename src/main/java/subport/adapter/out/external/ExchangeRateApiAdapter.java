package subport.adapter.out.external;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.out.LoadExchangeRatePort;

@Component
@RequiredArgsConstructor
public class ExchangeRateApiAdapter implements LoadExchangeRatePort {

	private final RestClient exchangeRateClient;

	@Value("${koreaexim.authkey}")
	private String authKey;

	@Value("${koreaexim.data}")
	private String data;

	@Override
	public BigDecimal load(String searchDate) {
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
			.filter(r -> r.currencyUnit().equals("USD"))
			.findFirst()
			.map(ExchangeRateResponse::exchangeRateToDecimal)
			.orElseThrow(() -> new CustomException(ErrorCode.USD_EXCHANGE_RATE_NOT_FOUND));
	}
}
