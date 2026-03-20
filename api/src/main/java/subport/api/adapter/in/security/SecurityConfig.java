package subport.api.adapter.in.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import subport.api.adapter.in.security.oauth2.CustomAuthorizationRequestResolver;
import subport.api.adapter.in.security.oauth2.CustomOAuth2UserService;
import subport.api.adapter.in.security.oauth2.CustomSuccessHandler;
import subport.common.constants.ClientUrlConstants;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomAuthorizationRequestResolver authorizationRequestResolver;
	private final CustomOAuth2UserService oAuth2UserService;
	private final CustomSuccessHandler successHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthFilter jwtAuthFilter;
	private final MdcFilter mdcFilter;

	@Bean
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(auth -> auth
					.authorizationRequestResolver(authorizationRequestResolver))
				.userInfoEndpoint(userInfo -> userInfo
					.userService(oAuth2UserService))
				.successHandler(successHandler))
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/refresh", "/h2-console/**", "/api/auth/guest").permitAll()
				.requestMatchers("/api/members/me", "/api/members/me/reminder-settings").hasRole("MEMBER")
				.anyRequest().authenticated())
			.addFilterAt(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(mdcFilter, JwtAuthFilter.class)
			.sessionManagement(s -> s
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.headers(headerConfig -> headerConfig
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(
			ClientUrlConstants.LOCAL_HTTP_ORIGIN,
			ClientUrlConstants.LOCAL_HTTPS_ORIGIN,
			ClientUrlConstants.PROD_ORIGIN
		));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("Authorization"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", configuration);

		return configurationSource;
	}
}
