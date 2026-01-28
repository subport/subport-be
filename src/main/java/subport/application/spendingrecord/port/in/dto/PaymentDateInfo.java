package subport.application.spendingrecord.port.in.dto;

public record PaymentDateInfo(
	boolean hasSpending,
	boolean hasSubscription
) {
}
