package subport.adapter.in.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Component
public class JwtManager {

	private static final Duration ACCESS_TOKEN_EXPIRATION_TIME = Duration.ofHours(1);
	private static final Duration REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofDays(30);

	private static final String BEARER_PREFIX = "Bearer ";

	private final SecretKey secretKey;

	public JwtManager(@Value("${jwt.secret}") String secret) {
		this.secretKey = new SecretKeySpec(
			secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm()
		);
	}

	public String createAccessToken(Long memberId, Instant now) {
		return createToken(
			memberId,
			now,
			ACCESS_TOKEN_EXPIRATION_TIME
		);
	}

	public String createRefreshToken(Long memberId, Instant now) {
		return createToken(
			memberId,
			now,
			REFRESH_TOKEN_EXPIRATION_TIME
		);
	}

	public Long getMemberId(String token) {
		return Long.valueOf(getAllClaims(token).getSubject());
	}

	public Instant getIssuedAt(String token) {
		return getAllClaims(token).getIssuedAt()
			.toInstant();
	}

	public Instant getExpiration(String token) {
		return getAllClaims(token).getExpiration()
			.toInstant();
	}

	// 인증 헤더 검증
	public void verifyAuthorizationHeader(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new CustomException(ErrorCode.INVALID_AUTHORIZATION_HEADER);
		}
	}

	// 토큰 만료 검증
	public void verifyTokenExpiration(String token, Instant now) {
		if (getExpiration(token).isBefore(now)) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		}
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

	private Claims getAllClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
