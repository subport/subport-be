package subport.api.application.auth.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.api.application.auth.port.in.AuthenticateAccessTokenUseCase;
import subport.api.application.auth.port.out.ExtractTokenClaimsPort;
import subport.api.application.exception.ApiErrorCode;
import subport.common.exception.CustomException;
import subport.common.jwt.dto.TokenClaims;
import subport.domain.token.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateAccessTokenService implements AuthenticateAccessTokenUseCase {

	private final ExtractTokenClaimsPort extractTokenClaimsPort;

	private static final String BEARER_PREFIX = "Bearer ";

	@Override
	public Long authenticateAndGetMemberId(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new CustomException(ApiErrorCode.INVALID_AUTHORIZATION_HEADER);
		}

		String accessToken = authorizationHeader.split(" ")[1];
		TokenClaims tokenClaims = extractTokenClaimsPort.extract(accessToken);

		if (!tokenClaims.role().equals(Role.USER.name())) {
			throw new CustomException(ApiErrorCode.FORBIDDEN_ACCESS);
		}

		return tokenClaims.subjectId();
	}
}
