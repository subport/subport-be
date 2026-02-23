package subport.common.exception;

import lombok.Getter;

@Getter
public class RefreshTokenExpiredException extends CustomException {

	private final ErrorCode errorCode;

	public RefreshTokenExpiredException(ErrorCode errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	public RefreshTokenExpiredException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
		this.errorCode = errorCode;
	}
}
