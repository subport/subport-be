package subport.adapter.in.web.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.adapter.in.web.AuthCookieProvider;
import subport.application.token.port.in.LogoutUseCase;
import subport.application.token.port.in.ReissueTokenUseCase;
import subport.application.token.port.in.TokenPair;
import subport.application.token.port.out.ReissueTokenResponse;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final ReissueTokenUseCase reissueTokenUseCase;
	private final LogoutUseCase logoutUseCase;

	@PostMapping("/refresh")
	public ResponseEntity<ReissueTokenResponse> refresh(@CookieValue(required = false) String refreshToken) {
		TokenPair tokenPair = reissueTokenUseCase.reissue(refreshToken);

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.createRefreshTokenCookie(tokenPair.RefreshToken()).toString()
			)
			.body(
				new ReissueTokenResponse(tokenPair.AccessToken())
			);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CookieValue(required = false) String refreshToken) {
		logoutUseCase.logout(refreshToken);

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.deleteRefreshTokenCookie().toString()
			)
			.build();
	}
}
