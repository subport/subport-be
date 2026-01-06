package subport.domain.subscription;

import lombok.Getter;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Getter
public enum SubscriptionAmountUnit {

	KRW("₩"),
	USD("$");

	private final String symbol;

	SubscriptionAmountUnit(String symbol) {
		this.symbol = symbol;
	}

	public static SubscriptionAmountUnit fromString(String amountUnit) {
		try {
			return SubscriptionAmountUnit.valueOf(amountUnit);
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_AMOUNT_UNIT);
		}
	}
}
