package subport.adapter.in.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import subport.adapter.common.AuthCookieProvider;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exception.ErrorResponse;
import subport.application.exception.RefreshTokenExpiredException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.from(errorCode));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleRefreshTokenExpiredException(RefreshTokenExpiredException e) {
		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(errorCode.getHttpStatus())
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.deleteRefreshTokenCookie().toString()
			)
			.body(ErrorResponse.from(errorCode));
	}
}
