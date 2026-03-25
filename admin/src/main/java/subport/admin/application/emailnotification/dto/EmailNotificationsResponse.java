package subport.admin.application.emailnotification.dto;

import java.util.List;

public record EmailNotificationsResponse(
	List<EmailNotificationResponse> notifications,
	int currentPage,
	long totalElements,
	int totalPages
) {
}
