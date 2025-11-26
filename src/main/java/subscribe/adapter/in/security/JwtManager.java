package subscribe.adapter.in.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import subscribe.application.exception.CustomException;
import subscribe.application.exception.ErrorCode;

@Component
public class JwtManager {

	private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L; // 1 hour
	private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L; // 1 day

	private static final String BEARER_PREFIX = "Bearer ";

	private final SecretKey secretKey;

	public JwtManager(@Value("${jwt.secret}") String secret) {
		this.secretKey = new SecretKeySpec(
			secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm()
		);
	}

	public String createAccessToken(String providerId, Date now) {
		return Jwts.builder()
			.subject(providerId)
			.issuedAt(now)
			.expiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(String providerId, Date now) {
		return Jwts.builder()
			.subject(providerId)
			.issuedAt(now)
			.expiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
			.signWith(secretKey)
			.compact();
	}

	public String getProviderId(String token) {
		return getAllClaims(token).getSubject();
	}

	public Date getIssuedAt(String token) {
		return getAllClaims(token).getIssuedAt();
	}

	public Date getExpiration(String token) {
		return getAllClaims(token).getExpiration();
	}

	// 인증 헤더 검증
	public void verifyAuthorizationHeader(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
			throw new CustomException(ErrorCode.INVALID_AUTHORIZATION_HEADER);
		}
	}

	// 토큰 만료 검증
	public void verifyTokenExpiration(String token, Date now) {
		if (getExpiration(token).before(now)) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		}
	}

	private Claims getAllClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
