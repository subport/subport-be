package subport.batch.persistence;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import subport.domain.token.RefreshToken;

public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	@Modifying
	@Query("""
		DELETE FROM RefreshToken r
		WHERE r.expiration <= :now
		""")
	int deleteRefreshTokensBefore(Instant now);
}
