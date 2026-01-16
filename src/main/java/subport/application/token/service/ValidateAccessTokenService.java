package subport.application.token.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.token.port.in.ValidateAccessTokenUseCase;
import subport.application.token.port.out.ExtractMemberIdPort;
import subport.application.token.port.out.ValidateTokenPort;

@Service
@RequiredArgsConstructor
public class ValidateAccessTokenService implements ValidateAccessTokenUseCase {

	private final ValidateTokenPort validateTokenPort;
	private final ExtractMemberIdPort extractMemberIdPort;

	private static final String BEARER_PREFIX = "Bearer ";

	@Override
	public Long validate(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new CustomException(ErrorCode.INVALID_AUTHORIZATION_HEADER);
		}

		String accessToken = authorizationHeader.split(" ")[1];
		validateTokenPort.validate(accessToken);

		return extractMemberIdPort.extractMemberId(accessToken);
	}
}
