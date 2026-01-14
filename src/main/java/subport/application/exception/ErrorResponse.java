package subport.application.exception;

public record ErrorResponse(
	int status,
	String error,
	String message
) {

	private ErrorResponse(ErrorCode errorCode) {
		this(
			errorCode.getHttpStatus().value(),
			errorCode.getHttpStatus().name(),
			errorCode.getMessage()
		);
	}

	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}
}
