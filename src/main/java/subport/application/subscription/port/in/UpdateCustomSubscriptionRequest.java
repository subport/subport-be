package subport.application.subscription.port.in;

public record UpdateCustomSubscriptionRequest(
	String name,
	String type
) {
}
