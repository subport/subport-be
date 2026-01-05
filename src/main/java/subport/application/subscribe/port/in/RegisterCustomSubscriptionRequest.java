package subport.application.subscribe.port.in;

public record RegisterCustomSubscriptionRequest(
	String name,
	String type,
	String logoImageUrl,
	Long memberId
) {
}
