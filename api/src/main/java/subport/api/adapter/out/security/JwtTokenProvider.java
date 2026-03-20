package subport.api.adapter.out.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import subport.api.application.auth.port.out.CreateAccessTokenPort;
import subport.api.application.auth.port.out.CreateRefreshTokenPort;
import subport.api.application.auth.port.out.ExtractTokenClaimsPort;
import subport.common.jwt.dto.TokenClaims;
import subport.domain.member.MemberRole;
import subport.domain.token.RefreshToken;
import subport.domain.token.RefreshTokenRole;

@Component
public class JwtTokenProvider implements
	CreateAccessTokenPort,
	CreateRefreshTokenPort,
	ExtractTokenClaimsPort {

	private static final Duration ACCESS_TOKEN_EXPIRATION_TIME = Duration.ofHours(1);
	private static final Duration REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofDays(30);

	private final SecretKey secretKey;
	private final JwtParser jwtParser;

	public JwtTokenProvider(@Value("${app.jwt.secret}") String secret) {
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
		Instant now,
		MemberRole role
	) {
		return createToken(
			subjectId,
			now,
			ACCESS_TOKEN_EXPIRATION_TIME,
			role.name()
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
		return Jwts.builder()
			.subject(subjectId.toString())
			.issuedAt(Date.from(now))
			.expiration(Date.from(now.plus(ttl)))
			.claim("role", role)
			.signWith(secretKey)
			.compact();
	}

	private Claims parseClaims(String token) {
		return jwtParser.parseSignedClaims(token)
			.getPayload();
	}
}
