package subport.application.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

	// 인증 관련
	INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "올바르지 않은 Authorization 헤더입니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "기한이 만료된 토큰입니다."),
	INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "토큰 형식이 올바르지 않습니다."),

	// 회원 관련
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

	// 이미지 파일 관련
	INVALID_IMAGE_FILE_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일만 업로드 가능합니다."),
	INVALID_IMAGE_FILE_SIZE(HttpStatus.BAD_REQUEST, "이미지 파일의 크기는 5MB를 초과할 수 없습니다."),
	FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일 읽기에 실패했습니다."),

	// 구독 관련
	INVALID_SUBSCRIPTION_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 구독 타입입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
