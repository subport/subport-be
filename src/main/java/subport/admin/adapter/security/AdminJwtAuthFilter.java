package subport.admin.adapter.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import subport.adapter.in.web.exception.ErrorResponse;
import subport.admin.application.service.AuthenticateAdminTokenService;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class AdminJwtAuthFilter extends OncePerRequestFilter {

	private static final List<String> EXCLUDE_PATTERNS = List.of("/admin/auth/login", "/admin/auth/refresh");
	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final AuthenticateAdminTokenService authenticateAdminTokenService;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		Long adminId;
		try {
			adminId = authenticateAdminTokenService.authenticateAndGetAdminId(
				request.getHeader("Authorization")
			);
		} catch (ExpiredJwtException e) {
			setErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
			return;
		} catch (JwtException e) {
			setErrorResponse(response, ErrorCode.INVALID_TOKEN_FORMAT);
			return;
		} catch (CustomException e) {
			setErrorResponse(response, e.getErrorCode());
			return;
		}

		AdminUserDetails adminUserDetails = new AdminUserDetails(adminId);
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(
				adminUserDetails,
				Optional.empty(),
				Collections.emptyList()
			)
		);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
		return EXCLUDE_PATTERNS.stream()
			.anyMatch(pattern -> pathMatcher.match(pattern, request.getServletPath()));
	}

	private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		ErrorResponse errorResponse = ErrorResponse.of(errorCode);
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
