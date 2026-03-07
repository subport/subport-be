package subport.admin.application.auth;

import java.time.Instant;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.account.LoadAdminPort;
import subport.admin.application.account.PasswordEncoderPort;
import subport.admin.application.account.dto.LoginAdminRequest;
import subport.admin.application.exception.AdminErrorCode;
import subport.admin.domain.Admin;
import subport.common.exception.CustomException;
import subport.common.exception.RefreshTokenExpiredException;
import subport.common.jwt.dto.TokenPair;
import subport.domain.token.RefreshToken;
import subport.domain.token.Role;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final LoadAdminPort loadAdminPort;
	private final PasswordEncoderPort passwordEncoderPort;
	private final CreateAccessTokenPort createAccessTokenPort;
	private final CreateRefreshTokenPort createRefreshTokenPort;
	private final RefreshTokenPort refreshTokenPort;

	public TokenPair login(LoginAdminRequest request, Instant now) {
		Admin admin = loadAdminPort.loadAdmin(request.email());

		if (!passwordEncoderPort.matches(request.password(), admin.getPassword())) {
			throw new CustomException(AdminErrorCode.ADMIN_PASSWORD_MISMATCH);
		}

		String accessToken = createAccessTokenPort.createAccessToken(admin.getId(), now, Role.ADMIN);
		RefreshToken refreshToken = createRefreshTokenPort.createRefreshToken(admin.getId(), now, Role.ADMIN);

		refreshTokenPort.save(refreshToken);

		admin.updateLastActiveAt(
			now.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()
		);

		return new TokenPair(
			accessToken,
			refreshToken.getTokenValue()
		);
	}

	public TokenPair reissue(String refreshTokenValue, Instant now) {
		if (refreshTokenValue == null) {
			throw new CustomException(AdminErrorCode.REFRESH_TOKEN_REQUIRED);
		}

		RefreshToken refreshToken = refreshTokenPort.load(refreshTokenValue);
		if (refreshToken.isExpired(now)) {
			refreshTokenPort.delete(refreshToken);
			throw new RefreshTokenExpiredException(AdminErrorCode.REFRESH_TOKEN_EXPIRED);
		}

		Long adminId = refreshToken.getSubjectId();
		String accessToken = createAccessTokenPort.createAccessToken(adminId, now, Role.ADMIN);

		RefreshToken newRefreshToken = createRefreshTokenPort.createRefreshToken(adminId, now, Role.ADMIN);
		refreshTokenPort.save(newRefreshToken);

		refreshTokenPort.delete(refreshToken);

		Admin admin = loadAdminPort.loadAdmin(adminId);
		admin.updateLastActiveAt(
			now.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()
		);

		return new TokenPair(accessToken, newRefreshToken.getTokenValue());
	}

	public void logout(String refreshTokenValue) {
		if (refreshTokenValue == null) {
			throw new CustomException(AdminErrorCode.REFRESH_TOKEN_REQUIRED);
		}

		refreshTokenPort.delete(refreshTokenValue);
	}
}
