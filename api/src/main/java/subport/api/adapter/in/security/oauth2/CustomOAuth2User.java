package subport.api.adapter.in.security.oauth2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subport.domain.member.MemberRole;

@RequiredArgsConstructor
@Getter
public class CustomOAuth2User implements OAuth2User {

	private final Long memberId;
	private final boolean firstLogin;
	private final MemberRole role;

	@Override
	public Map<String, Object> getAttributes() {
		return Map.of();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getName() {
		return memberId.toString();
	}
}