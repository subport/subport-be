package subport.common.jwt.dto;

public record TokenPair(
	String accessToken,
	String refreshToken
) {
}
