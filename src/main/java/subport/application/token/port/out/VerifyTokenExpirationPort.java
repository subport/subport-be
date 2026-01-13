package subport.application.token.port.out;

import java.time.Instant;

public interface VerifyTokenExpirationPort {

	void verifyTokenExpiration(String token, Instant now);
}
