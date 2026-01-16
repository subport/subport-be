package subport.application.token.port.in;

public record TokenPair(
	String AccessToken,
	String RefreshToken
) {
}
