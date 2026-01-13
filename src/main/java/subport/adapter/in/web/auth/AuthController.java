package subport.adapter.in.web.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.application.token.port.in.ReissueTokenUseCase;
import subport.application.token.port.out.ReissueTokenResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final ReissueTokenUseCase reissueTokenUseCase;

	@PostMapping("/refresh")
	public ResponseEntity<ReissueTokenResponse> refresh(@CookieValue(required = false) String refreshToken) {
		return ResponseEntity.ok(reissueTokenUseCase.reissue(refreshToken));
	}
}
