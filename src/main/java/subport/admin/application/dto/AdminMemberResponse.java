package subport.admin.application.dto;

import java.time.LocalDateTime;

public record AdminMemberResponse(
	Long id,
	String email,
	String nickname,
	Long memberSubscriptionCount,
	Long customSubscriptionCount,
	Integer reminderDaysBefore,
	LocalDateTime lastLoginAt,
	LocalDateTime createdAt,
	boolean deleted
) {
}
