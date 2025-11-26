package subscribe.adapter.in.security.oauth2;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import subscribe.adapter.in.security.JwtManager;
import subscribe.application.token.service.RefreshTokenCrudService;
import subscribe.domain.token.RefreshToken;

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
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		String providerId = oAuth2User.getName();

		String accessToken = jwtManager.createAccessToken(providerId, new Date());

		String refreshToken = jwtManager.createRefreshToken(providerId, new Date());
		tokenCrudService.saveRefreshToken(
			new RefreshToken(
				refreshToken,
				jwtManager.getProviderId(refreshToken),
				jwtManager.getIssuedAt(refreshToken),
				jwtManager.getExpiration(refreshToken)
			)
		);

		// 리다이렉트
		response.sendRedirect("temporary");
	}
}
