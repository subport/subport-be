package subport.adapter.in.security;

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
import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.exception.CustomException;
import subport.application.token.port.in.ValidateAccessTokenUseCase;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final List<String> EXCLUDE_PATTERNS = List.of("/api/auth/refresh");
	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final ValidateAccessTokenUseCase validateAccessTokenUseCase;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		Long memberId;
		try {
			memberId = validateAccessTokenUseCase.validate(
				request.getHeader("Authorization")
			);
		} catch (JwtException | CustomException e) {
			request.setAttribute("exception", e);
			filterChain.doFilter(request, response);
			return;
		}

		CustomOAuth2User oAuth2User = new CustomOAuth2User(memberId);
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
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return EXCLUDE_PATTERNS.stream()
			.anyMatch(pattern -> pathMatcher.match(pattern, request.getServletPath()));
	}
}
