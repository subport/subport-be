package subport.application.member.port.in.dto;

public record SyncMemberInfo(
	Long id,
	boolean firstLogin
) {
}
