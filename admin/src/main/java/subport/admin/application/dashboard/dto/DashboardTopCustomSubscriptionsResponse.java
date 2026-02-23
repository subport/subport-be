package subport.admin.application.dashboard.dto;

import java.util.List;

public record DashboardTopCustomSubscriptionsResponse(
	List<DashboardTopCustomSubscriptionResponse> topCustomSubscriptions
) {
}
