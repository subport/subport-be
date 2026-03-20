package subport.admin.adapter.out.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import subport.admin.application.auth.CreateAccessTokenPort;
import subport.admin.application.auth.CreateRefreshTokenPort;
import subport.admin.application.auth.ExtractTokenClaimsPort;
import subport.common.jwt.dto.TokenClaims;
import subport.domain.token.RefreshToken;
import subport.domain.token.RefreshTokenRole;

@Component
public class JwtTokenProvider implements
	CreateAccessTokenPort,
	CreateRefreshTokenPort,
	ExtractTokenClaimsPort {

	private static final Duration ACCESS_TOKEN_EXPIRATION_TIME = Duration.ofMinutes(15);
	private static final Duration REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofDays(3);

	private final SecretKey secretKey;
	private final JwtParser jwtParser;

	public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(
			secret.getBytes(StandardCharsets.UTF_8)
		);
		this.jwtParser = Jwts.parser()
			.verifyWith(secretKey)
			.build();
	}

	@Override
	public String createAccessToken(
		Long subjectId,
		Instant now
	) {
		return createToken(
			subjectId,
			now,
			ACCESS_TOKEN_EXPIRATION_TIME,
			null
		);
	}

	@Override
	public RefreshToken createRefreshToken(
		Long subjectId,
		Instant now,
		RefreshTokenRole role
	) {
		String refreshToken = createToken(
			subjectId,
			now,
			REFRESH_TOKEN_EXPIRATION_TIME,
			role.name()
		);
		Claims claims = parseClaims(refreshToken);

		return new RefreshToken(
			refreshToken,
			subjectId,
			role,
			claims.getIssuedAt().toInstant(),
			claims.getExpiration().toInstant()
		);
	}

	@Override
	public TokenClaims extract(String token) {
		Claims claims = parseClaims(token);
		String subjectId = claims.getSubject();
		String role = String.valueOf(claims.get("role"));

		return new TokenClaims(
			Long.valueOf(subjectId),
			role
		);
	}

	private String createToken(
		Long subjectId,
		Instant now,
		Duration ttl,
		String role
	) {
		JwtBuilder builder = Jwts.builder()
			.subject(subjectId.toString())
			.issuedAt(Date.from(now))
			.expiration(Date.from(now.plus(ttl)))
			.signWith(secretKey);

		if (role != null) {
			builder.claim("role", role);
		}

		return builder.compact();
	}

	private Claims parseClaims(String token) {
		return jwtParser.parseSignedClaims(token)
			.getPayload();
	}
}
