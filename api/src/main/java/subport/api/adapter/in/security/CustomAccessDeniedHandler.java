package subport.api.adapter.in.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.api.adapter.in.security.oauth2.CustomOAuth2User;
import subport.api.application.exception.ApiErrorCode;
import subport.common.exception.ErrorCode;
import subport.common.exception.ErrorResponse;
import subport.domain.member.MemberRole;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void handle(
		HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException accessDeniedException
	) throws IOException {
		String requestURI = request.getRequestURI();
		String method = request.getMethod();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getPrincipal() instanceof CustomOAuth2User oAuth2User) {
			if (oAuth2User.getRole() == MemberRole.GUEST) {
				log.warn("[AUTH] Unauthorized access: GUEST cannot access {} {}", method, requestURI);
				setErrorResponse(response, ApiErrorCode.GUEST_ACCESS_FORBIDDEN);
				return;
			}

			log.warn("[AUTH] Unauthorized access: role={} cannot access {} {}",
				oAuth2User.getRole(),
				method,
				requestURI
			);
			setErrorResponse(response, ApiErrorCode.FORBIDDEN);
			return;
		}

		log.warn("[AUTH] Unauthorized access: No authentication for {} {}", method, requestURI);
		setErrorResponse(response, ApiErrorCode.UNAUTHORIZED);
	}

	private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getStatus());
		response.setContentType("application/json;charset=UTF-8");

		ErrorResponse errorResponse = ErrorResponse.of(errorCode);
		response.getWriter().print(objectMapper.writeValueAsString(errorResponse));
	}
}
