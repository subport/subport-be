package subport.application.exception;

public class RefreshTokenExpiredException extends CustomException {

	public RefreshTokenExpiredException(Throwable cause) {
		super(ErrorCode.REFRESH_TOKEN_EXPIRED, cause);
	}
}
