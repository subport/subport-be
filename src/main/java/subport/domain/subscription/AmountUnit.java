package subport.domain.subscription;

import lombok.Getter;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Getter
public enum AmountUnit {

	KRW("₩"),
	USD("$");

	private final String symbol;

	AmountUnit(String symbol) {
		this.symbol = symbol;
	}

	public static AmountUnit fromString(String amountUnit) {
		try {
			return AmountUnit.valueOf(amountUnit);
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_AMOUNT_UNIT);
		}
	}
}
