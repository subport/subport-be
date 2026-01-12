package subport.adapter.in.security;

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
import subport.adapter.in.security.oauth2.CustomOAuth2UserService;
import subport.adapter.in.security.oauth2.CustomSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService oAuth2UserService;
	private final CustomSuccessHandler successHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo
					.userService(oAuth2UserService))
				.successHandler(successHandler))
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(authenticationEntryPoint))
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll())
			.addFilterAt(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.sessionManagement(s -> s
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.headers(headerConfig -> headerConfig
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("https://localhost:5173"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("Authorization"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", configuration);

		return configurationSource;
	}
}
