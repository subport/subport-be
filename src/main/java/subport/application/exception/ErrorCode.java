package subport.application.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

	// 인증 관련
	INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "올바르지 않은 Authorization 헤더입니다."),
	REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 Refresh 토큰입니다."),
	ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "기한이 만료된 Access 토큰입니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "기한이 만료된 Refresh 토큰입니다."),
	REFRESH_TOKEN_NOT_NULL(HttpStatus.UNAUTHORIZED, "Refresh 토큰 값은 필수입니다."),
	INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "토큰 형식이 올바르지 않습니다."),

	// 요청 데이터 검증 관련
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
	INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP 메서드입니다."),
	MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

	// 서버 에러
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 문제가 발생했습니다."),

	// 회원 관련
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

	// 이미지 파일 관련
	INVALID_IMAGE_FILE_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일만 업로드 가능합니다."),
	INVALID_IMAGE_FILE_SIZE(HttpStatus.BAD_REQUEST, "이미지 파일의 크기는 5MB를 초과할 수 없습니다."),
	FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일 읽기에 실패했습니다."),

	// 구독 서비스 관련
	SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 구독 서비스입니다."),
	INVALID_SUBSCRIPTION_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 구독 타입입니다."),
	SYSTEM_SUBSCRIPTION_WRITE_FORBIDDEN(HttpStatus.FORBIDDEN, "시스템 기본 제공 구독 서비스는 수정 및 삭제가 불가능합니다."),
	SUBSCRIPTION_WRITE_FORBIDDEN(HttpStatus.FORBIDDEN, "본인이 등록한 구독 서비스가 아니면 수정 및 삭제가 불가능합니다."),
	SUBSCRIPTION_READ_FORBIDDEN(HttpStatus.FORBIDDEN, "시스템 기본 제공이 아니면 본인이 등록한 구독 서비스만 조회가 가능합니다."),
	SUBSCRIPTION_USE_FORBIDDEN(HttpStatus.FORBIDDEN, "시스템 기본 제공 구독 서비스이거나 본인이 등록한 구독 서비스만 사용이 가능합니다."),

	// 구독 정보 관련
	MEMBER_SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 구독 정보입니다."),
	MEMBER_SUBSCRIPTION_FORBIDDEN(HttpStatus.FORBIDDEN, "본인의 구독 정보만 조회, 수정, 삭제가 가능합니다."),
	INVALID_MEMBER_SUBSCRIPTION_PLAN(HttpStatus.BAD_REQUEST, "해당 구독 서비스에 속하지 않은 플랜으로는 변경이 불가능합니다."),
	DUTCH_PAY_AMOUNT_MISSING(HttpStatus.BAD_REQUEST, "더치페이를 선택했으면 더치페이 금액을 반드시 입력해야 합니다."),
	DUTCH_PAY_AMOUNT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "더치페이를 선택하지 않았으면 금액을 입력할 수 없습니다."),
	INVALID_START_DATE_FUTURE(HttpStatus.BAD_REQUEST, "시작 날짜는 미래일 수 없습니다."),
	INVALID_START_DATE_TOO_OLD(HttpStatus.BAD_REQUEST, "시작 날짜는 최근 1년 이내로만 설정 가능합니다."),

	// 플랜 관련
	PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 플랜입니다."),
	INVALID_AMOUNT_UNIT(HttpStatus.BAD_REQUEST, "유효하지 않은 통화 단위입니다."),
	SYSTEM_PLAN_WRITE_FORBIDDEN(HttpStatus.FORBIDDEN, "시스템 기본 제공 플랜은 수정 및 삭제가 불가능합니다."),
	PLAN_WRITE_FORBIDDEN(HttpStatus.FORBIDDEN, "본인이 등록한 플랜이 아니면 수정 및 삭제가 불가능합니다."),
	PLAN_READ_FORBIDDEN(HttpStatus.FORBIDDEN, "시스템 기본 제공이 아니면 본인이 등록한 플랜만 조회가 가능합니다."),
	PLAN_USE_FORBIDDEN(HttpStatus.FORBIDDEN, "시스템 기본 제공 플랜이거나 본인이 등록한 플랜만 사용이 가능합니다."),

	// 소비 내역 관련
	SPENDING_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 소비 내역입니다."),
	SPENDING_RECORD_FORBIDDEN(HttpStatus.FORBIDDEN, "본인의 소비 내역만 삭제가 가능합니다."),

	// 어드민
	SUBSCRIPTION_IN_USE(HttpStatus.CONFLICT, "회원이 사용 중인 구독은 삭제할 수 없습니다."),
	PLAN_IN_USE(HttpStatus.CONFLICT, "회원이 사용 중인 플랜은 삭제할 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
