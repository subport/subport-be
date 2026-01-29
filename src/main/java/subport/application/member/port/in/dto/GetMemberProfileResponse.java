package subport.application.member.port.in.dto;

public record GetMemberProfileResponse(
	String nickname,
	long joinedDays
) {
}
