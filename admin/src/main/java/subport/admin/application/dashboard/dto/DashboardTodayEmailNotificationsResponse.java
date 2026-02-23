package subport.admin.application.dashboard.dto;

import java.util.List;

public record DashboardTodayEmailNotificationsResponse(
	List<DashboardTodayEmailNotificationResponse> notifications,
	long successCount,
	long failedCount,
	long pendingCount
) {
}
