package subport.admin.application.subscription.dto;

import java.util.List;

public record SubscriptionsResponse(
	List<SubscriptionResponse> subscriptions,
	int currentPage,
	long totalElements,
	int totalPages
) {
}