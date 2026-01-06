package subport.application.subscription.port.in;

public record RegisterCustomSubscriptionRequest(
	String name,
	String type
) {
}
