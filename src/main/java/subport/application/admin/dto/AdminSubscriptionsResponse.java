package subport.application.admin.dto;

import java.util.List;

public record AdminSubscriptionsResponse(
	List<AdminSubscriptionResponse> subscriptions
) {
}