package subport.api.application.spendingrecord.port.in.dto;

public record PaymentDateInfo(
	boolean hasSpending,
	boolean hasSubscription
) {
}
