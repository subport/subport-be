package subport.adapter.in.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;
import subport.adapter.in.web.AuthCookieProvider;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exception.RefreshTokenExpiredException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.warn("Business exception: {}", errorCode.name());

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(RefreshTokenExpiredException.class)
	public ResponseEntity<ErrorResponse> handleRefreshTokenExpiredException(RefreshTokenExpiredException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.warn("Refresh token expired");

		return ResponseEntity.status(errorCode.getHttpStatus())
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.deleteRefreshTokenCookie().toString()
			)
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		log.warn("Validation failed: {}", e.getBindingResult().getAllErrors());

		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode, e.getBindingResult()));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
		MissingServletRequestParameterException e) {
		log.warn("Missing required parameter: parameter='{}', type='{}'",
			e.getParameterName(),
			e.getParameterType());

		ErrorCode errorCode = ErrorCode.MISSING_REQUEST_PARAMETER;

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
		log.warn("Method argument type mismatch: parameter='{}', value='{}', requiredType='{}'",
			e.getName(),
			e.getValue(),
			e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");

		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
		log.warn("HTTP message not readable: {}", e.getMessage());

		ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException e) {
		log.warn("No resource found: {}", e.getMessage());

		ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
		log.warn("Method not supported: {}", e.getMessage());

		ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;

		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("Unexpected exception occurred", e);

		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode));
	}
}
