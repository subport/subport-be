package subport.admin.application.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subport.common.exception.ErrorCode;

@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode {

	// 인증 관련
	INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "올바르지 않은 Authorization 헤더입니다."),
	REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 Refresh 토큰입니다."),
	ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "기한이 만료된 Access 토큰입니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "기한이 만료된 Refresh 토큰입니다."),
	REFRESH_TOKEN_NOT_NULL(HttpStatus.UNAUTHORIZED, "Refresh 토큰 값은 필수입니다."),
	INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "토큰 형식이 올바르지 않습니다."),
	FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

	// 서버 에러
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 문제가 발생했습니다."),

	// 요청 데이터 검증 관련
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
	INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP 메서드입니다."),
	MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

	// 이미지 파일 관련
	IMAGE_FILE_REQUIRED(HttpStatus.BAD_REQUEST, "이미지 파일은 필수입니다."),
	INVALID_IMAGE_FILE_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일만 업로드 가능합니다."),
	INVALID_IMAGE_FILE_SIZE(HttpStatus.BAD_REQUEST, "이미지 파일의 크기는 5MB를 초과할 수 없습니다."),
	FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일 읽기에 실패했습니다."),

	// 구독 서비스 관련
	SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 구독 서비스입니다."),

	// 플랜 관련
	PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 플랜입니다."),

	// 어드민
	SUBSCRIPTION_IN_USE(HttpStatus.CONFLICT, "회원이 사용 중인 구독은 삭제할 수 없습니다."),
	PLAN_IN_USE(HttpStatus.CONFLICT, "회원이 사용 중인 플랜은 삭제할 수 없습니다."),
	ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다."),
	ADMIN_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	ADMIN_CURRENT_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다."),
	ADMIN_NEW_PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");

	private final HttpStatus httpStatus;

	@Getter
	private final String message;

	@Override
	public int getStatus() {
		return httpStatus.value();
	}

	@Override
	public String getError() {
		return httpStatus.name();
	}

	@Override
	public String getCode() {
		return this.name();
	}
}
