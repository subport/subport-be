package subport.admin.adapter;

import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.web.AuthCookieProvider;
import subport.admin.application.dto.AdminLoginRequest;
import subport.admin.application.service.AdminAuthService;
import subport.application.token.port.in.dto.TokenPair;
import subport.application.token.port.in.dto.TokenResponse;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

	private final AdminAuthService adminAuthService;

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody AdminLoginRequest request) {
		TokenPair tokenPair = adminAuthService.login(request, Instant.now());

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.createRefreshTokenCookie(tokenPair.refreshToken()).toString()
			)
			.body(
				new TokenResponse(tokenPair.accessToken())
			);
	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(@CookieValue(required = false) String refreshToken) {
		TokenPair tokenPair = adminAuthService.reissue(refreshToken, Instant.now());

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.createRefreshTokenCookie(tokenPair.refreshToken()).toString()
			)
			.body(
				new TokenResponse(tokenPair.accessToken())
			);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CookieValue(required = false) String refreshToken) {
		adminAuthService.logout(refreshToken);

		return ResponseEntity.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.deleteRefreshTokenCookie().toString()
			)
			.build();
	}
}
