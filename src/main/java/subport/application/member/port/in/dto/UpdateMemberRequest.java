package subport.application.member.port.in.dto;

public record UpdateMemberRequest(
	String nickname,
	String email
) {
}
