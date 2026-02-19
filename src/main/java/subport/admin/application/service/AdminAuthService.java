package subport.admin.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminLoginRequest;
import subport.admin.application.port.AdminRefreshTokenPort;
import subport.admin.application.port.LoadAdminPort;
import subport.admin.application.port.PasswordEncoderPort;
import subport.admin.domain.Admin;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exception.RefreshTokenExpiredException;
import subport.application.token.port.in.dto.TokenPair;
import subport.application.token.port.out.CreateAccessTokenPort;
import subport.application.token.port.out.CreateRefreshTokenPort;
import subport.domain.token.RefreshToken;
import subport.domain.token.Role;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminAuthService {

	private final LoadAdminPort loadAdminPort;
	private final PasswordEncoderPort passwordEncoderPort;
	private final CreateAccessTokenPort createAccessTokenPort;
	private final CreateRefreshTokenPort createRefreshTokenPort;
	private final AdminRefreshTokenPort refreshTokenPort;

	public TokenPair login(AdminLoginRequest request, Instant now) {
		Admin admin = loadAdminPort.loadAdmin(request.email());

		if (!passwordEncoderPort.matches(request.password(), admin.getPassword())) {
			throw new CustomException(ErrorCode.ADMIN_PASSWORD_MISMATCH);
		}

		String accessToken = createAccessTokenPort.createAccessToken(admin.getId(), now, Role.ADMIN);
		RefreshToken refreshToken = createRefreshTokenPort.createRefreshToken(admin.getId(), now, Role.ADMIN);

		refreshTokenPort.save(refreshToken);

		return new TokenPair(
			accessToken,
			refreshToken.getTokenValue()
		);
	}

	public TokenPair reissue(String refreshTokenValue, Instant now) {
		if (refreshTokenValue == null) {
			throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_NULL);
		}

		RefreshToken refreshToken = refreshTokenPort.load(refreshTokenValue);
		if (refreshToken.isExpired(now)) {
			refreshTokenPort.delete(refreshToken);
			throw new RefreshTokenExpiredException();
		}

		Long adminId = refreshToken.getSubjectId();
		String accessToken = createAccessTokenPort.createAccessToken(adminId, now, Role.ADMIN);

		RefreshToken newRefreshToken = createRefreshTokenPort.createRefreshToken(adminId, now, Role.ADMIN);
		refreshTokenPort.save(newRefreshToken);

		refreshTokenPort.delete(refreshToken);

		return new TokenPair(accessToken, newRefreshToken.getTokenValue());
	}
}
