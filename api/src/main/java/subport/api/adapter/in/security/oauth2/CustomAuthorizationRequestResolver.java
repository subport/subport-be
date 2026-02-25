package subport.api.adapter.in.security.oauth2;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import subport.common.constants.ClientUrlConstants;

@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository repository) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
			repository, "/oauth2/authorization"
		);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request);
		return customize(request, authRequest);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request, clientRegistrationId);
		return customize(request, authRequest);
	}

	private OAuth2AuthorizationRequest customize(
		HttpServletRequest request,
		OAuth2AuthorizationRequest authRequest
	) {
		if (authRequest == null) {
			return null;
		}

		String origin = request.getHeader(HttpHeaders.ORIGIN);
		if (origin == null) {
			String referer = request.getHeader(HttpHeaders.REFERER);
			if (referer == null) {
				origin = ClientUrlConstants.PROD_ORIGIN;
			} else {
				origin = referer;
			}
		}

		String encodedState = Base64.getUrlEncoder()
			.encodeToString(origin.getBytes(StandardCharsets.UTF_8));

		return OAuth2AuthorizationRequest.from(authRequest)
			.state(encodedState)
			.build();
	}
}
