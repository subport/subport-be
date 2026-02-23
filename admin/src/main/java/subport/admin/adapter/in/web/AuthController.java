package subport.admin.adapter.in.web;

import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.account.dto.LoginAdminRequest;
import subport.admin.application.auth.AuthService;
import subport.common.jwt.dto.AccessTokenResponse;
import subport.common.jwt.dto.TokenPair;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginAdminRequest request) {
		TokenPair tokenPair = authService.login(request, Instant.now());

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.createRefreshTokenCookie(tokenPair.refreshToken()).toString()
			)
			.body(
				new AccessTokenResponse(tokenPair.accessToken())
			);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AccessTokenResponse> refresh(@CookieValue(required = false) String refreshToken) {
		TokenPair tokenPair = authService.reissue(refreshToken, Instant.now());

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.createRefreshTokenCookie(tokenPair.refreshToken()).toString()
			)
			.body(
				new AccessTokenResponse(tokenPair.accessToken())
			);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CookieValue(required = false) String refreshToken) {
		authService.logout(refreshToken);

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.deleteRefreshTokenCookie().toString()
			)
			.build();
	}
}
