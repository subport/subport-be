package subport.api.application.auth.port.in;

import subport.api.application.auth.port.in.dto.AuthMemberInfo;

public interface AuthenticateAccessTokenUseCase {

	AuthMemberInfo authenticateAndGetMemberId(String authorizationHeader);
}
