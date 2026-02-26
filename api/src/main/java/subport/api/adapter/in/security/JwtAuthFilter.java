package subport.api.adapter.in.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.api.adapter.in.security.oauth2.CustomOAuth2User;
import subport.api.application.auth.port.in.AuthenticateAccessTokenUseCase;
import subport.common.exception.CustomException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final List<String> EXCLUDE_PATTERNS = List.of("/api/auth/refresh", "/h2-console/**");
	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final AuthenticateAccessTokenUseCase authenticateAccessTokenUseCase;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		log.info("In doFilterInternal : {}", request.getRequestURI());
		log.error("In doFilterInternal : {}", request.getRequestURI());
		System.out.println("In doFilterInternal : " + request.getRequestURI());
		Long memberId;
		try {
			System.out.println(request.getRequestURI());
			memberId = authenticateAccessTokenUseCase.authenticateAndGetMemberId(
				request.getHeader("Authorization")
			);
		} catch (JwtException | CustomException e) {
			request.setAttribute("exception", e);
			filterChain.doFilter(request, response);
			return;
		}

		CustomOAuth2User oAuth2User = new CustomOAuth2User(memberId, false);
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(
				oAuth2User,
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
}
