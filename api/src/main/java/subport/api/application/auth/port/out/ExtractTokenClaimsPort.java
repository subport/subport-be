package subport.api.application.auth.port.out;

import subport.common.jwt.dto.TokenClaims;

public interface ExtractTokenClaimsPort {

	TokenClaims extract(String token);
}
