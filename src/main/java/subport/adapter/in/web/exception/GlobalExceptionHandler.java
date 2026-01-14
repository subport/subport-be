package subport.adapter.in.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletResponse;
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
	public ResponseEntity<ErrorResponse> handleRefreshTokenExpiredException(
		RefreshTokenExpiredException e,
		HttpServletResponse response
	) {
		response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshTokenCookie().toString());

		ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.from(errorCode));
	}

	private ResponseCookie deleteRefreshTokenCookie() {
		return ResponseCookie.from("refreshToken", "")
			.path("/")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.maxAge(0)
			.build();
	}
}
