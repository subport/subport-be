package subport.admin.application.dto;

import java.util.List;

public record AdminSubscriptionsResponse(
	List<AdminSubscriptionResponse> subscriptions,
	int currentPage,
	long totalElements,
	int totalPages
) {
}