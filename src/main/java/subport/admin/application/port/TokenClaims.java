package subport.admin.application.port;

import subport.domain.token.Role;

public record TokenClaims(
	Long subjectId,
	Role role
) {
}
