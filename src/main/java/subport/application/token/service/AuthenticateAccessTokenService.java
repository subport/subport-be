package subport.application.token.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.token.port.in.AuthenticateAccessTokenUseCase;
import subport.application.token.port.out.ExtractSubjectIdPort;

@Service
@RequiredArgsConstructor
public class AuthenticateAccessTokenService implements AuthenticateAccessTokenUseCase {

	private final ExtractSubjectIdPort extractSubjectIdPort;

	private static final String BEARER_PREFIX = "Bearer ";

	@Override
	public Long authenticateAndGetMemberId(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new CustomException(ErrorCode.INVALID_AUTHORIZATION_HEADER);
		}

		String accessToken = authorizationHeader.split(" ")[1];

		return extractSubjectIdPort.extractSubjectId(accessToken);
	}
}
