package subport.application.exception;

public record ErrorResponse(
	int status,
	String error,
	String message
) {

	public ErrorResponse(ErrorCode errorCode) {
		this(
			errorCode.getHttpStatus().value(),
			errorCode.getHttpStatus().name(),
			errorCode.getMessage()
		);
	}
}
