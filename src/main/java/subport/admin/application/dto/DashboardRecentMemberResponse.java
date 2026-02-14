package subport.admin.application.dto;

import java.time.LocalDateTime;

public record DashboardRecentMemberResponse(
	String nickname,
	String email,
	long memberSubscriptionCount,
	LocalDateTime createdAt
) {
}
