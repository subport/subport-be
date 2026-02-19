package subport.adapter.in.security.oauth2;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import subport.adapter.in.web.AuthCookieProvider;
import subport.application.token.port.in.IssueTokenUseCase;
import subport.application.token.port.in.dto.TokenPair;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final IssueTokenUseCase issueTokenUseCase;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		Long memberId = Long.valueOf(oAuth2User.getName());

		TokenPair tokenPair = issueTokenUseCase.issue(memberId, Instant.now());

		String accessToken = URLEncoder.encode(tokenPair.accessToken(), StandardCharsets.UTF_8);
		String redirectUrl = UriComponentsBuilder
			.fromUriString("https://localhost:5173/login-success")
			.queryParam("access", accessToken)
			.queryParam("firstLogin", oAuth2User.isFirstLogin())
			.build()
			.toUriString();

		response.addHeader(
			HttpHeaders.SET_COOKIE,
			AuthCookieProvider.createRefreshTokenCookie(tokenPair.refreshToken()).toString()
		);
		response.sendRedirect(redirectUrl);
	}
}
