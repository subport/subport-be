package subport.admin.application.dto;

import java.util.List;

public record DashboardTodayEmailNotificationsResponse(
	List<DashboardTodayEmailNotificationResponse> notifications,
	long successCount,
	long failedCount,
	long pendingCount
) {
}
