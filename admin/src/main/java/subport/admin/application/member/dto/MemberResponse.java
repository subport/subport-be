package subport.admin.application.member.dto;

import java.time.LocalDateTime;

public record MemberResponse(
	Long id,
	String email,
	String nickname,
	Long memberSubscriptionCount,
	Long customSubscriptionCount,
	Integer reminderDaysBefore,
	LocalDateTime lastActiveAt,
	LocalDateTime createdAt,
	boolean deleted
) {
}
