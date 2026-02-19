package subport.adapter.out.security;

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
import subport.application.token.port.out.CreateAccessTokenPort;
import subport.application.token.port.out.CreateRefreshTokenPort;
import subport.application.token.port.out.ExtractMemberIdPort;
import subport.domain.token.RefreshToken;

@Component
public class JwtTokenProvider implements
	CreateAccessTokenPort,
	CreateRefreshTokenPort,
	ExtractMemberIdPort {

	private static final Duration ACCESS_TOKEN_EXPIRATION_TIME = Duration.ofHours(1);
	private static final Duration REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofDays(30);

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
	public String createAccessToken(Long memberId, Instant now) {
		return createToken(
			memberId,
			now,
			ACCESS_TOKEN_EXPIRATION_TIME
		);
	}

	@Override
	public RefreshToken createRefreshToken(Long memberId, Instant now) {
		String refreshToken = createToken(
			memberId,
			now,
			REFRESH_TOKEN_EXPIRATION_TIME
		);
		Claims claims = parseClaims(refreshToken);

		return new RefreshToken(
			refreshToken,
			memberId,
			claims.getIssuedAt().toInstant(),
			claims.getExpiration().toInstant()
		);
	}

	@Override
	public Long extractMemberId(String token) {
		return Long.valueOf(
			parseClaims(token).getSubject()
		);
	}

	private String createToken(
		Long memberId,
		Instant now,
		Duration ttl
	) {
		return Jwts.builder()
			.subject(memberId.toString())
			.issuedAt(Date.from(now))
			.expiration(Date.from(now.plus(ttl)))
			.signWith(secretKey)
			.compact();
	}

	private Claims parseClaims(String token) {
		return jwtParser.parseSignedClaims(token)
			.getPayload();
	}
}
