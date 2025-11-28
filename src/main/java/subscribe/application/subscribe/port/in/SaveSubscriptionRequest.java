package subscribe.application.subscribe.port.in;

import java.time.LocalDate;

public record SaveSubscriptionRequest(
	String name,
	Long typeId,
	Long planId,
	int headCount,
	LocalDate startAt,
	LocalDate endAt,
	String memo
) {
}
