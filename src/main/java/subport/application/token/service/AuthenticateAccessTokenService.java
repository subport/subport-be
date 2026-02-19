package subport.application.token.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.TokenClaims;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.token.port.in.AuthenticateAccessTokenUseCase;
import subport.application.token.port.out.ExtractTokenClaimsPort;
import subport.domain.token.Role;

@Service
@RequiredArgsConstructor
public class AuthenticateAccessTokenService implements AuthenticateAccessTokenUseCase {

	private final ExtractTokenClaimsPort extractTokenClaimsPort;

	private static final String BEARER_PREFIX = "Bearer ";

	@Override
	public Long authenticateAndGetMemberId(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new CustomException(ErrorCode.INVALID_AUTHORIZATION_HEADER);
		}

		String accessToken = authorizationHeader.split(" ")[1];
		TokenClaims tokenClaims = extractTokenClaimsPort.extract(accessToken);

		if (tokenClaims.role() != Role.USER) {
			throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
		}

		return tokenClaims.subjectId();
	}
}
