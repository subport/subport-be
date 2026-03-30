package subport.api.adapter.in.web.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;
import subport.api.adapter.in.web.AuthCookieProvider;
import subport.api.application.exception.ApiErrorCode;
import subport.common.exception.CustomException;
import subport.common.exception.ErrorCode;
import subport.common.exception.ErrorResponse;
import subport.common.exception.RefreshTokenExpiredException;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.warn("[BIZ] code={}, message={}", errorCode.getCode(), errorCode.getMessage(), e);

		return ResponseEntity.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(RefreshTokenExpiredException.class)
	public ResponseEntity<ErrorResponse> handleRefreshTokenExpiredException(RefreshTokenExpiredException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.warn("[AUTH] Refresh token expired");

		return ResponseEntity.status(errorCode.getStatus())
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.deleteRefreshTokenCookie().toString()
			)
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		Map<String, String> fieldErrors = extractFieldErrors(e.getBindingResult());
		log.warn("[VALIDATION] errors={}", fieldErrors);

		ErrorCode errorCode = ApiErrorCode.INVALID_INPUT_VALUE;

		return ResponseEntity.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode, fieldErrors));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		MissingServletRequestParameterException e
	) {
		log.warn("[PARAM] Missing parameter='{}', type='{}'", e.getParameterName(), e.getParameterType());

		ErrorCode errorCode = ApiErrorCode.MISSING_REQUEST_PARAMETER;

		return ResponseEntity.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e
	) {
		log.warn("[PARAM] Type mismatch: parameter='{}', value='{}', requiredType='{}'",
			e.getName(),
			e.getValue(),
			e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");

		ErrorCode errorCode = ApiErrorCode.INVALID_INPUT_VALUE;

		return ResponseEntity.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.warn("[REQUEST] Unreadable body: {}", e.getMessage());

		ErrorCode errorCode = ApiErrorCode.UNREADABLE_REQUEST_BODY;

		return ResponseEntity.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ignored) {
		ErrorCode errorCode = ApiErrorCode.RESOURCE_NOT_FOUND;

		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e
	) {
		log.warn("[ROUTE] Method not allowed: {}", e.getMessage());

		ErrorCode errorCode = ApiErrorCode.METHOD_NOT_ALLOWED;

		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.warn("[UPLOAD] File size exceeded: {}", e.getMessage());

		ApiErrorCode errorCode = ApiErrorCode.IMAGE_FILE_SIZE_EXCEEDED;

		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleRemainingExceptions(Exception e) {
		log.error("[SYSTEM] Unexpected exception: {}", e.getMessage(), e);

		ErrorCode errorCode = ApiErrorCode.INTERNAL_SERVER_ERROR;

		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	private static Map<String, String> extractFieldErrors(BindingResult bindingResult) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return errors;
	}
}
