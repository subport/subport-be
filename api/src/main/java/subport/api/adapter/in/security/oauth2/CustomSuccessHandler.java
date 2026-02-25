package subport.api.adapter.in.security.oauth2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import subport.api.adapter.in.web.AuthCookieProvider;
import subport.api.application.auth.port.in.IssueTokenUseCase;
import subport.common.constants.ClientUrlConstants;
import subport.common.jwt.dto.TokenPair;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final String LOCAL_HOST = "localhost";
	private static final String PROD_HOST = "subport.site";

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

		String state = request.getParameter("state");
		String origin = ClientUrlConstants.PROD_ORIGIN;
		if (state != null) {
			try {
				String decoded = new String(
					Base64.getUrlDecoder().decode(state),
					StandardCharsets.UTF_8
				);

				URI uri = new URI(decoded);
				String host = uri.getHost();

				if (LOCAL_HOST.equals(host)) {
					origin = ClientUrlConstants.LOCAL_HTTPS_ORIGIN;
				}
				if (PROD_HOST.equals(host)) {
					origin = ClientUrlConstants.PROD_ORIGIN;
				}
			} catch (IllegalArgumentException | URISyntaxException ignored) {
			}
		}

		String accessToken = URLEncoder.encode(tokenPair.accessToken(), StandardCharsets.UTF_8);
		String redirectUrl = UriComponentsBuilder
			.fromUriString(origin + ClientUrlConstants.LOGIN_SUCCESS_PATH)
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
