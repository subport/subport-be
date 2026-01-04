package subport.application.member.port.in;

public record UpdateMemberRequest(
	String nickname,
	String email
) {
}
