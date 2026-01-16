package subport.application.token.port.in.dto;

public record TokenPair(
	String AccessToken,
	String RefreshToken
) {
}
