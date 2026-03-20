package subport.api.adapter.in.web.auth;

import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.api.adapter.in.web.AuthCookieProvider;
import subport.api.application.auth.port.in.GuestLoginUseCase;
import subport.api.application.auth.port.in.LogoutUseCase;
import subport.api.application.auth.port.in.ReissueTokenUseCase;
import subport.common.jwt.dto.AccessTokenResponse;
import subport.common.jwt.dto.ReissueTokenResponse;
import subport.common.jwt.dto.TokenPair;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final ReissueTokenUseCase reissueTokenUseCase;
	private final LogoutUseCase logoutUseCase;
	private final GuestLoginUseCase guestLoginUseCase;

	@PostMapping("/refresh")
	public ResponseEntity<ReissueTokenResponse> refresh(@CookieValue(required = false) String refreshToken) {
		TokenPair tokenPair = reissueTokenUseCase.reissue(refreshToken, Instant.now());

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.createRefreshTokenCookie(tokenPair.refreshToken()).toString()
			)
			.body(
				new ReissueTokenResponse(tokenPair.accessToken())
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

	@PostMapping("/guest")
	public ResponseEntity<AccessTokenResponse> guestLogin() {
		return ResponseEntity.ok()
			.body(guestLoginUseCase.login(Instant.now()));
	}
}
