package subport.application.membersubscription.port.out;

import java.math.BigDecimal;

public interface LoadExchangeRatePort {

	BigDecimal load(String searchDate);
}
