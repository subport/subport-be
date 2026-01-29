package subport.adapter.in.security.oauth2;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
		OAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		Long memberId = Long.valueOf(oAuth2User.getName());

		TokenPair tokenPair = issueTokenUseCase.issue(memberId, Instant.now());

		String url = String.format(
			"%s?access=%s",
			"https://localhost:5173" + "/login-success",
			URLEncoder.encode(tokenPair.AccessToken(), StandardCharsets.UTF_8)
		);

		response.addHeader(
			HttpHeaders.SET_COOKIE,
			AuthCookieProvider.createRefreshTokenCookie(tokenPair.RefreshToken()).toString()
		);
		response.sendRedirect(url);
	}
}
