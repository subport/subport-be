package subport.adapter.out.external;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Bean
	public RestClient exchangeRateClient() {
		return RestClient.builder()
			.baseUrl("https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON")
			.build();
	}
}
