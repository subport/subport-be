package subport.domain.subscription;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public enum AmountUnit {

	KRW("₩", "원"),
	USD("$", "달러");

	private final String symbol;
	private final String displayName;

	public static AmountUnit fromString(String amountUnit) {
		try {
			return AmountUnit.valueOf(amountUnit);
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_AMOUNT_UNIT);
		}
	}
}
