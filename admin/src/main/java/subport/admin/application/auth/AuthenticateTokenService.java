package subport.admin.application.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.admin.application.exception.AdminErrorCode;
import subport.common.jwt.dto.TokenClaims;
import subport.domain.token.Role;

@Service
@RequiredArgsConstructor
public class AuthenticateTokenService {

	private final ExtractTokenClaimsPort extractTokenClaimsPort;

	private static final String BEARER_PREFIX = "Bearer ";

	public Long authenticateAndGetAdminId(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new subport.common.exception.CustomException(AdminErrorCode.INVALID_AUTHORIZATION_HEADER);
		}

		String accessToken = authorizationHeader.split(" ")[1];
		TokenClaims tokenClaims = extractTokenClaimsPort.extract(accessToken);

		if (!tokenClaims.role().equals(Role.ADMIN.name())) {
			throw new subport.common.exception.CustomException(AdminErrorCode.FORBIDDEN_ACCESS);
		}

		return tokenClaims.subjectId();
	}
}
