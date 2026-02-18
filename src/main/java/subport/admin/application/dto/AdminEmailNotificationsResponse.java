package subport.admin.application.dto;

import java.util.List;

public record AdminEmailNotificationsResponse(
	List<AdminEmailNotificationResponse> notifications,
	int currentPage,
	long totalElements,
	int totalPages
) {
}
