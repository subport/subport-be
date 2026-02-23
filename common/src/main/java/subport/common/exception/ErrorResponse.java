package subport.common.exception;

import java.util.Map;

public record ErrorResponse(
	int status,
	String error,
	String code,
	String message,
	Map<String, String> fieldErrors
) {

	private ErrorResponse(ErrorCode errorCode, Map<String, String> fieldErrors) {
		this(
			errorCode.getStatus(),
			errorCode.getError(),
			errorCode.getCode(),
			errorCode.getMessage(),
			fieldErrors
		);
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode, null);
	}

	public static ErrorResponse of(ErrorCode errorCode, Map<String, String> fieldErrors) {
		return new ErrorResponse(errorCode, fieldErrors);
	}
}
