package subport.batch.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.batch.persistence.SpringDataRefreshTokenRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

	private final SpringDataRefreshTokenRepository refreshTokenRepository;

	public void deleteExpiredRefreshTokens(Instant now) {
		int deletedCount = refreshTokenRepository.deleteRefreshTokensBefore(now);

		log.info("만료된 RefreshToken 삭제 완료 - 기준 시각: {}, 삭제 수: {}", now, deletedCount);
	}
}
