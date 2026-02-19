package subport.application.token.port.out;

import subport.admin.application.port.TokenClaims;

public interface ExtractTokenClaimsPort {

	TokenClaims extract(String token);
}
