package subport.api.adapter.in.security;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import subport.api.adapter.in.security.oauth2.CustomOAuth2User;

@Component
public class MdcFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		try {
			MDC.put("requestId", UUID.randomUUID().toString().substring(0, 8));
			MDC.put("method", request.getMethod());
			MDC.put("uri", request.getRequestURI());

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User user) {
				MDC.put("memberId", String.valueOf(user.getMemberId()));
			} else {
				MDC.put("memberId", "anonymous");
			}

			filterChain.doFilter(request, response);
		} finally {
			MDC.clear();
		}
	}
}
