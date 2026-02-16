package subport.admin.application.dto;

import java.util.List;

public record DashboardTopCustomSubscriptionsResponse(
	List<DashboardTopCustomSubscriptionResponse> topCustomSubscriptions
) {
}
