package subport.adapter.in.web.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import subport.application.exception.ErrorCode;

public record ErrorResponse(
	int status,
	String error,
	String code,
	String message,
	Map<String, String> fieldErrors
) {

	private ErrorResponse(ErrorCode errorCode, Map<String, String> fieldErrors) {
		this(
			errorCode.getHttpStatus().value(),
			errorCode.getHttpStatus().name(),
			errorCode.name(),
			errorCode.getMessage(),
			fieldErrors
		);
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode, null);
	}

	public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
		return new ErrorResponse(errorCode, extractFieldErrors(bindingResult));
	}

	private static Map<String, String> extractFieldErrors(BindingResult bindingResult) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return errors;
	}
}
