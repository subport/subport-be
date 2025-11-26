package subscribe.application.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

	// 인증 관련
	INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "올바르지 않은 Authorization 헤더입니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "기한이 만료된 토큰입니다."),
	INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "토큰 형식이 올바르지 않습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
