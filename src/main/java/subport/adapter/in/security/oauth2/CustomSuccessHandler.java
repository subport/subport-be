package subport.adapter.in.security.oauth2;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.JwtManager;
import subport.application.token.service.RefreshTokenCrudService;
import subport.domain.token.RefreshToken;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtManager jwtManager;
	private final RefreshTokenCrudService tokenCrudService;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException {
		OAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		Long memberId = Long.valueOf(oAuth2User.getName());

		String accessToken = jwtManager.createAccessToken(memberId, new Date());

		String refreshToken = jwtManager.createRefreshToken(memberId, new Date());
		tokenCrudService.saveRefreshToken(
			new RefreshToken(
				refreshToken,
				jwtManager.getMemberId(refreshToken),
				jwtManager.getIssuedAt(refreshToken),
				jwtManager.getExpiration(refreshToken)
			)
		);

		String url = String.format(
			"%s?access=%s",
			"http://localhost:5173" + "/login-success",
			URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
		);

		response.addCookie(createRefreshTokenCookie(refreshToken));
		response.sendRedirect(url);
	}

	private Cookie createRefreshTokenCookie(String refreshToken) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true);
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setMaxAge(24 * 60 * 60);
		return refreshTokenCookie;
	}
}
