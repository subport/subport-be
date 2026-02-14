package subport.admin.application.query;

public record CustomMemberSubscriptionCount(
	String normalizedSubscriptionName,
	String subscriptionName,
	long memberSubscriptionCount
) {
}
