package subport.domain.plan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AmountUnit {

	KRW("₩", "원"),
	USD("$", "달러");

	private final String symbol;
	private final String displayName;
}
