package subport.adapter.in.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import subport.adapter.in.web.exception.ErrorResponse;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {
		Object exception = request.getAttribute("exception");

		if (exception instanceof ExpiredJwtException) {
			setErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
			return;
		}

		// ExpiredJwtException을 제외한 나머지 JwtException 처리
		if (exception instanceof JwtException) {
			setErrorResponse(response, ErrorCode.INVALID_TOKEN_FORMAT);
			return;
		}

		if (exception instanceof CustomException) {
			setErrorResponse(response, ((CustomException)exception).getErrorCode());
		}
	}

	private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		ErrorResponse errorResponse = ErrorResponse.of(errorCode);
		response.getWriter().print(objectMapper.writeValueAsString(errorResponse));
	}
}
