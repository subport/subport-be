package subport.common.jwt.dto;

public record TokenClaims(
	Long subjectId,
	String role
) {
}
