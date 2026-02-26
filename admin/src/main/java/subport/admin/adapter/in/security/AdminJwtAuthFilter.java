package subport.admin.adapter.in.security;

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
import lombok.extern.slf4j.Slf4j;
import subport.admin.application.auth.AuthenticateTokenService;
import subport.admin.application.exception.AdminErrorCode;
import subport.common.exception.ErrorCode;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminJwtAuthFilter extends OncePerRequestFilter {

	private static final List<String> EXCLUDE_PATTERNS = List.of(
		"/admin/auth/login",
		"/admin/auth/refresh",
		"/h2-console/**"
	);
	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final AuthenticateTokenService authenticateTokenService;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		log.info("In doFilterInternal : {}", request.getRequestURI());
		log.error("In doFilterInternal : {}", request.getRequestURI());
		System.out.println("In doFilterInternal : " + request.getRequestURI());
		Long adminId;
		try {
			adminId = authenticateTokenService.authenticateAndGetAdminId(
				request.getHeader("Authorization")
			);
		} catch (ExpiredJwtException e) {
			setErrorResponse(response, AdminErrorCode.ACCESS_TOKEN_EXPIRED);
			return;
		} catch (JwtException e) {
			setErrorResponse(response, AdminErrorCode.INVALID_TOKEN_FORMAT);
			return;
		} catch (subport.common.exception.CustomException e) {
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
		response.setStatus(errorCode.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		subport.common.exception.ErrorResponse errorResponse = subport.common.exception.ErrorResponse.of(errorCode);
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
