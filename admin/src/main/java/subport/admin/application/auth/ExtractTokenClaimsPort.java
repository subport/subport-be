package subport.admin.application.auth;

import subport.common.jwt.dto.TokenClaims;

public interface ExtractTokenClaimsPort {

	TokenClaims extract(String accessToken);
}
